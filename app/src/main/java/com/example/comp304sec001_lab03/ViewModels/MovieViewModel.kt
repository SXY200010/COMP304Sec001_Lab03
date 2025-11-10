//Xiaoyu Shi and Angelica Cuadrado - Lab 3 assignment

package com.example.comp304sec001_lab03.ViewModels

//Holds and manages the app’s data and business rules.
// It connects the UI to the Room database through the repository,
// keeps data alive across configuration changes, and validates inputs
// before saving.

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp304sec001_lab03.data.Movie
import com.example.comp304sec001_lab03.data.MovieDatabase
import com.example.comp304sec001_lab03.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class MovieFormState(
    val id: String = "",
    val title: String = "",
    val director: String = "",
    val price: String = "",
    val releaseDateIso: String = "",
    val durationMinutes: String = "",
    val genre: String = "",
    val favorite: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    internal val repo = MovieRepository(MovieDatabase.getDatabase(application).movieDao())

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _favorites = MutableStateFlow<List<Movie>>(emptyList())
    val favorites: StateFlow<List<Movie>> = _favorites

    private val _form = MutableStateFlow(MovieFormState())
    val form: StateFlow<MovieFormState> = _form

    init {
        viewModelScope.launch {
            repo.allMovies.collectLatest { _movies.value = it }
        }
        viewModelScope.launch {
            repo.favoriteMovies.collectLatest { _favorites.value = it }
        }
        // Optional: seed 3 movies on first launch if DB empty
        viewModelScope.launch {
            if (_movies.value.isEmpty()) {
                seedInitialMovies()
            }
        }
    }

    private suspend fun seedInitialMovies() {
        val seed = listOf(
            Movie(101, "Inception", "Christopher Nolan", 19.99, "2010-07-16", 148, "Thriller", false),
            Movie(202, "Spirited Away", "Hayao Miyazaki", 14.99, "2001-07-20", 125, "Family", true),
            Movie(303, "Mad Max: Fury Road", "George Miller", 17.49, "2015-05-15", 120, "Action", false)
        )
        seed.forEach {
            if (!repo.exists(it.id)) repo.add(it)
        }
    }

    fun updateForm(transform: (MovieFormState) -> MovieFormState) {
        _form.value = transform(_form.value).copy(error = null, successMessage = null)
    }

    fun submitMovie() {
        val f = _form.value
        val id = f.id.toIntOrNull()
        val price = f.price.toDoubleOrNull()
        val duration = f.durationMinutes.toIntOrNull()

        val error = when {
            id == null || id !in 100..999 -> "ID must be two digits between 11 and 99."
            f.title.isBlank() -> "Title is required."
            f.director.isBlank() -> "Director is required."
            price == null || price <= 0.0 -> "Price must be a positive number."
            f.releaseDateIso.isBlank() -> "Release date is required (YYYY-MM-DD)."
            duration == null || duration <= 0 -> "Duration must be a positive integer."
            f.genre.isBlank() -> "Genre is required."
            else -> null
        }

        if (error != null) {
            _form.value = f.copy(error = error, successMessage = null)
            return
        }

        viewModelScope.launch {
            if (repo.exists(id!!)) {
                _form.value = f.copy(error = "A movie with ID $id already exists.", successMessage = null)
                return@launch
            }
            val movie = Movie(
                id = id,
                title = f.title.trim(),
                director = f.director.trim(),
                price = price!!,
                releaseDateIso = f.releaseDateIso.trim(),
                durationMinutes = duration!!,
                genre = f.genre.trim(),
                favorite = f.favorite
            )
            repo.add(movie)
            _form.value = MovieFormState(successMessage = "Movie added.", favorite = f.favorite)
        }
    }

    fun updateMovie(movie: Movie) {
        val f = _form.value
        val id = f.id.toIntOrNull()
        val price = f.price.toDoubleOrNull()
        val duration = f.durationMinutes.toIntOrNull()

        val error = when {
            id == null || id !in 100..999 -> "ID must be between 100 and 999."
            f.title.isBlank() -> "Title is required."
            f.director.isBlank() -> "Director is required."
            price == null || price <= 0.0 -> "Price must be a positive number."
            f.releaseDateIso.isBlank() -> "Release date is required (YYYY-MM-DD)."
            duration == null || duration <= 0 -> "Duration must be a positive integer."
            f.genre.isBlank() -> "Genre is required."
            else -> null
        }

        if (error != null) {
            _form.value = f.copy(error = error, successMessage = null)
            return
        }

        viewModelScope.launch {
            val updated = Movie(
                id = id!!,
                title = f.title.trim(),
                director = f.director.trim(),
                price = price!!,
                releaseDateIso = f.releaseDateIso.trim(),
                durationMinutes = duration!!,
                genre = f.genre.trim(),
                favorite = f.favorite
            )
            repo.update(updated)
            _form.value = f.copy(successMessage = "Movie updated successfully!", error = null)
        }
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch { repo.delete(movie) }
}