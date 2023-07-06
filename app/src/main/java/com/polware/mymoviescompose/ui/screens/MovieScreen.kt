package com.polware.mymoviescompose.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.polware.mymoviescompose.components.MovieAppBar
import com.polware.mymoviescompose.components.MovieDetails
import com.polware.mymoviescompose.data.model.MovieModel
import com.polware.mymoviescompose.ui.MovieViewModel
import com.polware.mymoviescompose.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MovieScreen(
    movieViewModel: MovieViewModel,
    selectedMovie: MovieModel?,
    navigateToMainListScreen: (Action) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            MovieAppBar(
                navigateToMainListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToMainListScreen(action)
                    } else {
                        if (movieViewModel.validateFields()) {
                            navigateToMainListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }
                },
                selectedMovie = selectedMovie
            )
        }
    ) {
        MovieDetails(
            title = movieViewModel.title.value,
            onTitleChange = movieViewModel::onTitleChange,
            description = movieViewModel.description.value,
            onDescriptionChange = movieViewModel::onDescriptionChange,
            genre = movieViewModel.genre.value,
            onGenreSelected = {
                movieViewModel.onGenreSelected(it)
            },
            year = movieViewModel.year.value,
            onYearChange = movieViewModel::onYearChange,
            score = movieViewModel.score.value,
            onScoreChange = movieViewModel::onScoreChange
        )
    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "No text fields should be empty",
        Toast.LENGTH_SHORT
    ).show()
}