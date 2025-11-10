package com.example.comp304sec001_lab03.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.comp304sec001_lab03.ViewModels.MovieViewModel
import com.example.comp304sec001_lab03.ui.screen.AddMovieScreen
import com.example.comp304sec001_lab03.ui.screen.EditMovieScreen
import com.example.comp304sec001_lab03.ui.screen.HomeScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    viewModel: MovieViewModel
) {
    val isExpanded = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val startDestination = if (isExpanded) "home_detail" else "home"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            HomeScreen(navController, viewModel, windowSizeClass)
        }
        composable("home_detail") {
            HomeScreen(navController, viewModel, windowSizeClass)
        }
        composable("add") {
            AddMovieScreen(navController, viewModel, windowSizeClass)
        }
        composable(
            route = "edit/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            EditMovieScreen(
                movieId = movieId,
                navController = navController,
                viewModel = viewModel,
                windowSizeClass = windowSizeClass
            )
        }
    }
}

