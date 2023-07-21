package com.polware.mymoviescompose.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.polware.mymoviescompose.data.model.Genre
import com.polware.mymoviescompose.data.model.MovieModel
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
    val uriImage = Uri.parse(movieModel.image)

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
        Row(
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 3.dp)
                    .weight(3.5f)
            ) {
                AsyncImage(
                    model = uriImage,
                    modifier = Modifier
                        .size(160.dp),
                    contentDescription = "Movie image",
                )
            }
            Column(
                modifier = Modifier
                    .padding(all = 4.dp)
                    .weight(6.5f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movieModel.title,
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movieModel.description,
                    color = MaterialTheme.colors.taskItemTextColor,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    MovieRating(score = movieModel.score)
                }
            }
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
            image = "",
            title = "Title",
            genre = Genre.ANIMATION,
            year = "2010",
            description = "Some random text",
            score = 7
        ),
        navigateToMovieScreen = {}
    )
}