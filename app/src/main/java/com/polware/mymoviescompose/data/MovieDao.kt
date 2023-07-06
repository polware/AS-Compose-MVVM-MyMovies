package com.polware.mymoviescompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.polware.mymoviescompose.data.model.MovieModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // The OnConflictStrategy.IGNORE strategy ignores a new item if it's primary key is already in the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(movie: MovieModel)

    @Update
    suspend fun updateMovie(movie: MovieModel)

    @Delete
    suspend fun deleteMovie(movie: MovieModel)

    @Query("DELETE from movie_table")
    suspend fun deleteAllMovies()

    // Using Flow or LiveData as return type will ensure you get notified whenever the data in the database changes
    @Query("SELECT * from movie_table WHERE id = :movieId")
    fun getSelectedMovie(movieId: Int): Flow<MovieModel>

    @Query("SELECT * from movie_table ORDER BY id ASC")
    fun getAllMovies(): Flow<List<MovieModel>>

    @Query("SELECT * from movie_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery:String): Flow<List<MovieModel>>

}