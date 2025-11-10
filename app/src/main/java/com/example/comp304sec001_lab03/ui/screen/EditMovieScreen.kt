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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovieScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    movieId: Int,
    windowSizeClass: WindowSizeClass
) {
    val scope = rememberCoroutineScope()
    val form by viewModel.form.collectAsState()

    // 当进入编辑页面时，自动将选中电影信息填入表单
    LaunchedEffect(movieId) {
        val movie = viewModel.repo.getById(movieId)
        if (movie != null) {
            viewModel.updateForm {
                it.copy(
                    id = movie.id.toString(),
                    title = movie.title,
                    director = movie.director,
                    price = movie.price.toString(),
                    releaseDateIso = movie.releaseDateIso,
                    durationMinutes = movie.durationMinutes.toString(),
                    genre = movie.genre,
                    favorite = movie.favorite
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Movie #$movieId") },
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
                    //error
                    item {
                        if (form.error != null)
                            Text(form.error!!, color = MaterialTheme.colorScheme.error)
                    }

                    // title
                    item {
                        OutlinedTextField(
                            value = form.title,
                            onValueChange = { newValue ->
                                viewModel.updateForm { it.copy(title = newValue) }
                            },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // director
                    item {
                        OutlinedTextField(
                            value = form.director,
                            onValueChange = { newValue ->
                                viewModel.updateForm { it.copy(director = newValue) }
                            },
                            label = { Text("Director") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Price
                    item {
                        OutlinedTextField(
                            value = form.price,
                            onValueChange = { newValue ->
                                viewModel.updateForm { it.copy(price = newValue) }
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
                                viewModel.updateForm { it.copy(releaseDateIso = newDate) }
                            }
                        )
                    }

                    // Duration
                    item {
                        OutlinedTextField(
                            value = form.durationMinutes,
                            onValueChange = { newValue ->
                                viewModel.updateForm { it.copy(durationMinutes = newValue) }
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
                                viewModel.updateForm { it.copy(genre = newGenre) }
                            }
                        )
                    }

                    // Favorite
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = form.favorite,
                                onCheckedChange = { checked ->
                                    viewModel.updateForm { it.copy(favorite = checked) }
                                }
                            )
                            Text("Favorite movie")
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.updateMovie(
                                        com.example.comp304sec001_lab03.data.Movie(
                                            id = form.id.toIntOrNull() ?: 0,
                                            title = form.title,
                                            director = form.director,
                                            price = form.price.toDoubleOrNull() ?: 0.0,
                                            releaseDateIso = form.releaseDateIso,
                                            durationMinutes = form.durationMinutes.toIntOrNull() ?: 0,
                                            genre = form.genre,
                                            favorite = form.favorite
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save")
                            }

                            Button(
                                onClick = {
                                    val idInt = form.id.toIntOrNull()
                                    if (idInt != null) {
                                        scope.launch {
                                            val movie = viewModel.repo.getById(idInt)
                                            if (movie != null) {
                                                viewModel.deleteMovie(movie)
                                            }
                                            navController.popBackStack()
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete")
                            }
                        }

                        form.successMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
