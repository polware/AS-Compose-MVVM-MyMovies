package com.polware.mymoviescompose.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    fun changeAction(newAction: Action) {
        action.value = newAction
    }

    private val _allMovies = MutableStateFlow<RequestState<List<MovieModel>>>(RequestState.Idle)
    val allMovies: StateFlow<RequestState<List<MovieModel>>> = _allMovies

    // States
    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val genre: MutableState<Genre> = mutableStateOf((Genre.ACTION))
    val year: MutableState<String> = mutableStateOf("")
    val score: MutableState<Int> = mutableStateOf(0)

    init {
        getAllMovies()
    }

    private val _searchedMovies = MutableStateFlow<RequestState<List<MovieModel>>>(RequestState.Idle)
    val searchedMovies: StateFlow<RequestState<List<MovieModel>>> = _searchedMovies

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
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    // State: searchAppBarState
    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
        private set

    // State: searchTextState
    var searchTextState: MutableState<String> = mutableStateOf("")
        private set

    // onSearchClicked is an event we're defining that the UI can invoke
    // event: onSearchClicked
    fun onSearchClicked(newSearchAppBarState: SearchAppBarState) {
        searchAppBarState.value = newSearchAppBarState
    }

    // event: onSearchTextChanged
    fun onSearchTextChanged(newText: String) {
        searchTextState.value = newText
    }

    // Events for fields:
    fun onTitleChange(newTitle: String) {
        // limit characters
        if (newTitle.length < 40) {
            title.value = newTitle
        }
    }

    fun onDescriptionChange(newDescription: String) {
        description.value = newDescription
    }

    fun onGenreSelected(newGenre: Genre) {
        genre.value = newGenre
    }

    fun onYearChange(newYear: String) {
        year.value = newYear
    }

    fun onScoreChange(newScore: Int) {
        score.value = newScore
    }

    fun updateMovieFields(selectedMovie: MovieModel?) {
        if (selectedMovie != null) {
            id.value = selectedMovie.id
            title.value = selectedMovie.title
            genre.value = selectedMovie.genre
            year.value = selectedMovie.year
            description.value = selectedMovie.description
            score.value = selectedMovie.score
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            genre.value =Genre.ACTION
            year.value = ""
            score.value = 0
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && year.value.isNotEmpty() && description.value.isNotEmpty()
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

    private val _selectedMovie: MutableStateFlow<MovieModel?> = MutableStateFlow(null)
    val selectMovie: StateFlow<MovieModel?> = _selectedMovie

    fun getSelectedMovie(movieId: Int) {
        viewModelScope.launch {
            repository.getSelectedMovie(movieId = movieId).collect {
                _selectedMovie.value = it
            }
        }
    }

    private fun addMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val newMovie = MovieModel(
                title = title.value,
                genre = genre.value,
                year = year.value,
                description = description.value,
                score = score.value
            )
            repository.addMovie(movie = newMovie)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val modifyMovie = MovieModel(
                id = id.value,
                title = title.value,
                genre = genre.value,
                year = year.value,
                description = description.value,
                score = score.value
            )
            repository.updateMovie(movie = modifyMovie)
        }
    }

    private fun deleteMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = MovieModel(
                id = id.value,
                title = title.value,
                description = description.value,
                genre = genre.value,
                year = year.value,
                score = score.value
            )
            repository.deleteMovie(movie = movie)
        }
    }

    private fun deleteAllMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMovies()
        }
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
        this.action.value = Action.NO_ACTION
    }

}