package com.polware.mymoviescompose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.polware.mymoviescompose.data.model.Genre
import com.polware.mymoviescompose.data.model.MovieModel
import com.polware.mymoviescompose.ui.theme.LARGE_PADDING
import com.polware.mymoviescompose.ui.theme.MOVIE_ITEM_ELEVATION
import com.polware.mymoviescompose.ui.theme.taskItemBackgroundColor
import com.polware.mymoviescompose.ui.theme.taskItemTextColor
import com.polware.mymoviescompose.util.RequestState
import com.polware.mymoviescompose.util.SearchAppBarState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieListContent(
    navigateToMovieScreen: (taskId: Int) -> Unit,
    allMovies: RequestState<List<MovieModel>>,
    searchAppBarState: SearchAppBarState,
    searchedMovies: RequestState<List<MovieModel>>
) {
    when (searchAppBarState) {
        SearchAppBarState.TRIGGERED -> {
            if (searchedMovies is RequestState.Success) {
                HandleListContent(
                    movies = searchedMovies.data,
                    navigateToMovieScreen = navigateToMovieScreen
                )
            }
        }
        SearchAppBarState.CLOSED -> {
            if (allMovies is RequestState.Success) {
                HandleListContent(
                    movies = allMovies.data,
                    navigateToMovieScreen = navigateToMovieScreen
                )
            }
        }
        else -> {
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    movies: List<MovieModel>,
    navigateToMovieScreen: (movieId: Int) -> Unit
) {
    if (movies.isEmpty()) EmptyContent() else
        DisplayMovies(
            movies = movies,
            navigateToMovieScreen = navigateToMovieScreen
        )
}

@ExperimentalMaterialApi
@Composable
fun DisplayMovies(movies: List<MovieModel>, navigateToMovieScreen: (movieId: Int) -> Unit) {
    LazyColumn {
        items(
            items = movies,
            key = { movie -> movie.id }
        ) { movie ->
            MovieItem(
                movieModel = movie,
                navigateToMovieScreen = navigateToMovieScreen
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MovieItem(
    movieModel: MovieModel,
    navigateToMovieScreen: (movieId: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = MOVIE_ITEM_ELEVATION,
        onClick = {
            navigateToMovieScreen(movieModel.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        )
        {
            Row {
                Text(
                    modifier = Modifier.weight(8f),
                    text = movieModel.title,
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    MovieRating(score = movieModel.score)
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = movieModel.description,
                color = MaterialTheme.colors.taskItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun MovieItemPreview() {
    MovieItem(
        movieModel = MovieModel(
            id = 0,
            title = "Title",
            genre = Genre.ANIMATION,
            year = "2010",
            description = "Some random text",
            score = 7
        ),
        navigateToMovieScreen = {}
    )
}