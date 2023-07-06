package com.polware.mymoviescompose.di

import android.content.Context
import androidx.room.Room
import com.polware.mymoviescompose.data.MovieDao
import com.polware.mymoviescompose.data.MovieDatabase
import com.polware.mymoviescompose.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: MovieDatabase): MovieDao = database.movieDao()

}