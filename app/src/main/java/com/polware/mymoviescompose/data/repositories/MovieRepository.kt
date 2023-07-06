package com.polware.mymoviescompose.data.repositories

import com.polware.mymoviescompose.data.MovieDao
import com.polware.mymoviescompose.data.model.MovieModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class MovieRepository @Inject constructor(private val movieDao: MovieDao) {

    val getAllMovies: Flow<List<MovieModel>> = movieDao.getAllMovies()

    fun getSelectedMovie(movieId: Int): Flow<MovieModel> {
        return movieDao.getSelectedMovie(movieId = movieId)
    }

    suspend fun addMovie(movie: MovieModel) {
        movieDao.addMovie(movie = movie)
    }

    suspend fun updateMovie(movie: MovieModel) {
        movieDao.updateMovie(movie = movie)
    }

    suspend fun deleteMovie(movie: MovieModel) {
        movieDao.deleteMovie(movie = movie)
    }

    suspend fun deleteAllMovies() {
        movieDao.deleteAllMovies()
    }

    fun searchDatabase(searchQuery: String): Flow<List<MovieModel>> {
        return movieDao.searchDatabase(searchQuery = searchQuery)
    }

}