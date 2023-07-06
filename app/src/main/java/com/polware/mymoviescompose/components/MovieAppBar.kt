package com.polware.mymoviescompose.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.polware.mymoviescompose.R
import com.polware.mymoviescompose.data.model.Genre
import com.polware.mymoviescompose.data.model.MovieModel
import com.polware.mymoviescompose.ui.theme.topAppBarBackgroundColor
import com.polware.mymoviescompose.ui.theme.topAppBarContentColor
import com.polware.mymoviescompose.util.Action

@Composable
fun MovieAppBar(
    navigateToMainListScreen: (Action) -> Unit,
    selectedMovie: MovieModel?
) {
    if (selectedMovie == null)
        NewMovieAppBar(
            navigateToMainListScreen = navigateToMainListScreen
        )
    else ExistingMovieAppBar(
        selectedMovie = selectedMovie,
        navigateToMainListScreen = navigateToMainListScreen
    )
}

@Composable
fun NewMovieAppBar(
    navigateToMainListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToMainListScreen)
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_movie),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            AddAction(onAddClicked = navigateToMainListScreen)
        }
    )
}

@Composable
fun BackAction(onBackClicked: (Action) -> Unit) {
    IconButton(
        onClick = {
            onBackClicked(Action.NO_ACTION)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back Arrow",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(onAddClicked: (Action) -> Unit) {
    IconButton(
        onClick = {
            onAddClicked(Action.ADD)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = "Add Movie",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun ExistingMovieAppBar(
    selectedMovie: MovieModel,
    navigateToMainListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToMainListScreen)
        },
        title = {
            Text(
                text = selectedMovie.title,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ExistingMovieAppBarActions(
                selectedMovie = selectedMovie,
                navigateToMainListScreen = navigateToMainListScreen
            )
        }
    )
}

@Composable
fun ExistingMovieAppBarActions(
    selectedMovie: MovieModel,
    navigateToMainListScreen: (Action) -> Unit,
    ) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = "Remove ${selectedMovie.title}?",
        message = "Are you sure you want to remove ${selectedMovie.title}?",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { navigateToMainListScreen(Action.DELETE) }
    )
    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
    UpdateAction(onUpdateClicked = navigateToMainListScreen)
}

@Composable
fun CloseAction(onCloseClicked: (Action) -> Unit) {
    IconButton(
        onClick = {
            onCloseClicked(Action.NO_ACTION)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close Icon",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(onDeleteClicked: () -> Unit) {
    IconButton(
        onClick = {
            onDeleteClicked()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Icon",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(onUpdateClicked: (Action) -> Unit) {
    IconButton(
        onClick = {
            onUpdateClicked(Action.UPDATE)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Update,
            contentDescription = "Update Icon",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
@Preview
fun PreviewNewMovieAppBar() {
    NewMovieAppBar(navigateToMainListScreen = {})
}

@Composable
@Preview
fun PreviewExistingMovieAppBar() {
    ExistingMovieAppBar(
        navigateToMainListScreen = {},
        selectedMovie = MovieModel(
            id = 0,
            title = "Predator",
            genre = Genre.ACTION,
            year = "1989",
            description = "The best one",
            score = 9
        )
    )
}
