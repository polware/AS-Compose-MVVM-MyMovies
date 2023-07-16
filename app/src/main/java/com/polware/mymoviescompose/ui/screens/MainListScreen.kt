package com.polware.mymoviescompose.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.polware.mymoviescompose.R
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.polware.mymoviescompose.components.MovieListContent
import com.polware.mymoviescompose.components.AppMainToolbar
import com.polware.mymoviescompose.ui.MovieViewModel
import com.polware.mymoviescompose.ui.theme.fabBackgroundColor
import com.polware.mymoviescompose.util.Action
import com.polware.mymoviescompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainListScreen(
    navigateToMovieScreen: (taskId: Int) -> Unit,
    movieViewModel: MovieViewModel,
    action: Action
) {
    // To call suspend functions safely from inside a composable, use the LaunchedEffect API,
    // which triggers a coroutine-scoped side-effect in Compose
    val allMovies by movieViewModel.allMovies.collectAsState() // the type is  RequestState<List<TodoTask>>

    // 1. We are declaring a variable searchAppBarState of type SearchAppBarState
    // 2. sharedViewModel.searchAppBarState is of type MutableState<SearchAppBarState>
    // Which is an observable state holder. Since it's observable, it will tell compose
    // whenever it's updated so compose can recompose any composables that read it
    val searchAppBarState: SearchAppBarState = movieViewModel.searchAppBarState
    val searchTextState: String = movieViewModel.searchTextState
    val searchedMovies by movieViewModel.searchedMovies.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // Every time the MainListScreen function triggers the following function is triggered
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { movieViewModel.handleDatabaseActions(action) },
        taskTitle = movieViewModel.title,
        action = action,
        onUndoClicked = {
            movieViewModel.onChangeAction(it)
        },
    )
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppMainToolbar(
                movieViewModel = movieViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        floatingActionButton = {
            NewMovieFab(navigateToMovieScreen = navigateToMovieScreen)
        }
    ) {
        MovieListContent(
            navigateToMovieScreen = navigateToMovieScreen,
            allMovies = allMovies,
            searchAppBarState = searchAppBarState,
            searchedMovies = searchedMovies
        )
    }
}

@Composable
fun NewMovieFab(
    navigateToMovieScreen: (movieId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = { navigateToMovieScreen(-1) },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    action: Action,
    onUndoClicked: (Action) -> Unit
) {
    // Function triggered any time there is a recomposition of DisplaySnackBar composable
    handleDatabaseActions()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, movieTitle = taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setMessage(action: Action, movieTitle: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All movies removed!"
        else -> "$movieTitle has been ${action.name}"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "Ok"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}