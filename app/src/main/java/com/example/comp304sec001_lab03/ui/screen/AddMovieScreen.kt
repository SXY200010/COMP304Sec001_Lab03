package com.example.comp304sec001_lab03.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp304sec001_lab03.ViewModels.MovieViewModel
import com.example.comp304sec001_lab03.ui.components.DatePickerField
import com.example.comp304sec001_lab03.ui.components.GenreDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    windowSizeClass: WindowSizeClass
) {
    val form by viewModel.form.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Movie") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                if (form.error != null)
                    Text(form.error!!, color = MaterialTheme.colorScheme.error)
            }

            // ID
            item {
                OutlinedTextField(
                    value = form.id,
                    onValueChange = { newId ->
                        viewModel.updateForm { current -> current.copy(id = newId) }
                    },
                    label = { Text("Movie ID (100–999)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Title
            item {
                OutlinedTextField(
                    value = form.title,
                    onValueChange = { newTitle ->
                        viewModel.updateForm { current -> current.copy(title = newTitle) }
                    },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Director
            item {
                OutlinedTextField(
                    value = form.director,
                    onValueChange = { newDirector ->
                        viewModel.updateForm { current -> current.copy(director = newDirector) }
                    },
                    label = { Text("Director") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Price
            item {
                OutlinedTextField(
                    value = form.price,
                    onValueChange = { newPrice ->
                        viewModel.updateForm { current -> current.copy(price = newPrice) }
                    },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Date
            item {
                DatePickerField(
                    dateText = form.releaseDateIso,
                    onDateSelected = { newDate ->
                        viewModel.updateForm { current -> current.copy(releaseDateIso = newDate) }
                    }
                )
            }

            // Duration
            item {
                OutlinedTextField(
                    value = form.durationMinutes,
                    onValueChange = { newDuration ->
                        viewModel.updateForm { current -> current.copy(durationMinutes = newDuration) }
                    },
                    label = { Text("Duration (minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Genre
            item {
                GenreDropdown(
                    selectedGenre = form.genre,
                    onGenreSelected = { newGenre ->
                        viewModel.updateForm { current -> current.copy(genre = newGenre) }
                    }
                )
            }

            // Favorite
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = form.favorite,
                        onCheckedChange = { checked ->
                            viewModel.updateForm { current -> current.copy(favorite = checked) }
                        }
                    )
                    Text("Favorite movie")
                }
            }

            // Add Button
            item {
                Button(
                    onClick = { viewModel.submitMovie() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Movie")
                }

                form.successMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
