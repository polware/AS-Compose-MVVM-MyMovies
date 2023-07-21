package com.polware.mymoviescompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.polware.mymoviescompose.ui.screens.MainListScreen
import com.polware.mymoviescompose.ui.MovieViewModel
import com.polware.mymoviescompose.ui.screens.SplashScreen
import com.polware.mymoviescompose.ui.screens.MovieScreen
import com.polware.mymoviescompose.util.Action
import com.polware.mymoviescompose.util.Constants.MAIN_ARGUMENT_KEY
import com.polware.mymoviescompose.util.Constants.MAIN_LIST_SCREEN
import com.polware.mymoviescompose.util.Constants.MOVIE_ARGUMENT_KEY
import com.polware.mymoviescompose.util.Constants.MOVIE_SCREEN
import com.polware.mymoviescompose.util.Constants.SPLASH_SCREEN
import com.polware.mymoviescompose.util.toAction

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    changeAction: (Action) -> Unit,
    action: Action
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        // Argument is provided inside routes in curly braces: {argument}
        composable(
            route = SPLASH_SCREEN,
            exitTransition = {
                when (targetState.destination.route) {
                    MAIN_LIST_SCREEN ->
                        slideOutHorizontally(targetOffsetX = { -1000 },
                            animationSpec = tween(300))
                    else -> null
                }
            },
        ) {
            SplashScreen(
                navigateToListScreen = {
                    navController.navigate(route = "main/${Action.NO_ACTION}") {
                        popUpTo(SPLASH_SCREEN) { inclusive = true }
                    }
                }
            )
        }
        // Main List Screen
        composable(
            route = MAIN_LIST_SCREEN,
            arguments = listOf(
                navArgument(MAIN_ARGUMENT_KEY) {
                    // Make argument type safe
                    type = NavType.StringType
                })
        ) {
            navBackStackEntry ->
            // Look up "action" in NavBackStackEntry's arguments
            val actionArgument =     navBackStackEntry.arguments?.getString(MAIN_ARGUMENT_KEY).toAction()
            // Define default value for user actions
            var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

            // Whenever myAction changes the block of code inside is triggered
            LaunchedEffect(key1 = myAction) {
                if (actionArgument != myAction) {
                    myAction = actionArgument
                    changeAction(myAction)
                }
            }
            MainListScreen(
                navigateToMovieScreen = {
                        movieId ->
                    navController.navigate(route = "movie/$movieId")
                },
                movieViewModel = movieViewModel,
                action = action,
            )
        }
        // Movie Screen (Details)
        composable(
            route = MOVIE_SCREEN,
            arguments = listOf(
                navArgument(MOVIE_ARGUMENT_KEY) {
                    // Make argument type safe
                    type = NavType.IntType
                }),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300))
            }
        ) {
            entry -> // Look up "movieId" in NavBackStackEntry's arguments
            val movieId = entry.arguments!!.getInt(MOVIE_ARGUMENT_KEY)

            LaunchedEffect(key1 = movieId, block = {
                movieViewModel.getSelectedMovie(movieId = movieId)
            })
            val selectedMovie by movieViewModel.selectMovie.collectAsState()
            // Everytime selectedMovie changes the block of code is triggered
            LaunchedEffect(key1 = selectedMovie) {
                if (selectedMovie != null || movieId == -1) {
                    movieViewModel.updateMovieFields(selectedMovie)
                }
            }
            MovieScreen(
                movieViewModel = movieViewModel,
                selectedMovie = selectedMovie,
                navigateToMainListScreen = { action ->
                    navController.navigate(route = "main/${action.name}") {
                        popUpTo(MAIN_LIST_SCREEN) { inclusive = true }
                    }
                }
            )
        }
    }
}