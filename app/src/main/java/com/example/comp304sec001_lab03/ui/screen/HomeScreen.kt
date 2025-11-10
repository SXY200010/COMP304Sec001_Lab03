package com.example.comp304sec001_lab03.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp304sec001_lab03.ViewModels.MovieViewModel
import com.example.comp304sec001_lab03.ui.screens.components.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    windowSizeClass: WindowSizeClass
) {
    val movies by viewModel.movies.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    var showFavorites by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Library") },
                actions = {
                    IconButton(onClick = { showFavorites = !showFavorites }) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = if (showFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { navController.navigate("add") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Movie")
                    }
                }
            )
        }
    ) { padding ->
        val list = if (showFavorites) favorites else movies

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { movie ->
                MovieItem(
                    movie = movie,
                    onEdit = { navController.navigate("edit/${movie.id}") },
                    onDelete = { viewModel.deleteMovie(movie) },
                    onToggleFavorite = {
                        viewModel.updateMovie(movie.copy(favorite = !movie.favorite))
                    }
                )
            }
        }
    }
}
