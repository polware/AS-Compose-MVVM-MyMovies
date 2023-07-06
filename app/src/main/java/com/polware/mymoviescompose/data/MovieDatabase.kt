package com.polware.mymoviescompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.polware.mymoviescompose.data.model.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

}