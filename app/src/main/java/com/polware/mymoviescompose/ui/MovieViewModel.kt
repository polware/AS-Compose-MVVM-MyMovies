package com.polware.mymoviescompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polware.mymoviescompose.data.model.Genre
import com.polware.mymoviescompose.data.model.MovieModel
import com.polware.mymoviescompose.data.repositories.MovieRepository
import com.polware.mymoviescompose.util.Action
import com.polware.mymoviescompose.util.RequestState
import com.polware.mymoviescompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository,): ViewModel() {

    private val _allMovies = MutableStateFlow<RequestState<List<MovieModel>>>(RequestState.Idle)
    val allMovies: StateFlow<RequestState<List<MovieModel>>> = _allMovies

    private val _selectedMovie: MutableStateFlow<MovieModel?> = MutableStateFlow(null)
    val selectMovie: StateFlow<MovieModel?> = _selectedMovie

    private val _searchedMovies = MutableStateFlow<RequestState<List<MovieModel>>>(RequestState.Idle)
    val searchedMovies: StateFlow<RequestState<List<MovieModel>>> = _searchedMovies

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    // State: searchAppBarState
    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    // State: searchTextState
    var searchTextState by mutableStateOf("")
        private set

    // States
    var id by mutableStateOf(0)
        private set
    var image by mutableStateOf("")
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var genre by mutableStateOf((Genre.ACTION))
        private set
    var year by mutableStateOf("")
        private set
    var score by mutableStateOf(0)
        private set

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        _allMovies.value = RequestState.Loading
        try {
            viewModelScope.launch {
                // Trigger the flow and consume its elements using collect
                repository.getAllMovies.collect {
                    _allMovies.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allMovies.value = RequestState.Error(e)
        }
    }

    fun getSelectedMovie(movieId: Int) {
        viewModelScope.launch {
            repository.getSelectedMovie(movieId = movieId).collect {
                _selectedMovie.value = it
            }
        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchedMovies.value = RequestState.Loading
        try {
            viewModelScope.launch {
                // Finds any values that have searchQuery in any position (SQL)
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedMovies.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedMovies.value = RequestState.Error(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    // Event: updates value of searchAppBarState
    fun updateSearchBarState(newSearchAppBarState: SearchAppBarState) {
        searchAppBarState = newSearchAppBarState
    }

    // Event: updates value of searchTextState
    fun onSearchTextChanged(newText: String) {
        searchTextState = newText
    }

    fun onImageChange(newImage: String) {
        image = newImage
    }

    // Events for fields:
    fun onTitleChange(newTitle: String) {
        // limit characters
        if (newTitle.length < 40) {
            title = newTitle
        }
    }

    fun onChangeAction(newAction: Action) {
        action = newAction
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }

    fun onGenreSelected(newGenre: Genre) {
        genre = newGenre
    }

    fun onYearChange(newYear: String) {
        year = newYear
    }

    fun onScoreChange(newScore: Int) {
        score = newScore
    }

    fun updateMovieFields(selectedMovie: MovieModel?) {
        if (selectedMovie != null) {
            id = selectedMovie.id
            image = selectedMovie.image
            title = selectedMovie.title
            genre = selectedMovie.genre
            year = selectedMovie.year
            description = selectedMovie.description
            score = selectedMovie.score
        } else {
            id = 0
            image = ""
            title = ""
            description = ""
            genre =Genre.ACTION
            year = ""
            score = 0
        }
    }

    fun validateFields(): Boolean {
        return image.isNotEmpty() && title.isNotEmpty() && year.isNotEmpty() && description.isNotEmpty()
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> addMovie()
            Action.UPDATE -> updateMovie()
            Action.DELETE -> deleteMovie()
            Action.DELETE_ALL -> deleteAllMovies()
            Action.UNDO -> addMovie()
            else -> {
            }
        }
        this.action = Action.NO_ACTION
    }

    private fun addMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val newMovie = MovieModel(
                image = image,
                title = title,
                genre = genre,
                year = year,
                description = description,
                score = score
            )
            repository.addMovie(movie = newMovie)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val modifyMovie = MovieModel(
                id = id,
                image = image,
                title = title,
                genre = genre,
                year = year,
                description = description,
                score = score
            )
            repository.updateMovie(movie = modifyMovie)
        }
    }

    private fun deleteMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = MovieModel(
                id = id,
                image = image,
                title = title,
                description = description,
                genre = genre,
                year = year,
                score = score
            )
            repository.deleteMovie(movie = movie)
        }
    }

    private fun deleteAllMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMovies()
        }
    }

}