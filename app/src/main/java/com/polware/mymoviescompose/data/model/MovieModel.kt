package com.polware.mymoviescompose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polware.mymoviescompose.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class MovieModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val genre: Genre,
    val year: String,
    val description: String,
    val score: Int
)
