//Xiaoyu Shi and Angelica Cuadrado - Lab 3 assignment

package com.example.comp304sec001_lab03.data

import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieDao) {
    val allMovies: Flow<List<Movie>> = dao.getAll()
    val favoriteMovies: Flow<List<Movie>> = dao.getFavorites()

    suspend fun add(movie: Movie) = dao.insert(movie)
    suspend fun update(movie: Movie) = dao.update(movie)
    suspend fun delete(movie: Movie) = dao.delete(movie)
    suspend fun exists(id: Int): Boolean = dao.exists(id)
    suspend fun getById(id: Int): Movie? = dao.getById(id)
}