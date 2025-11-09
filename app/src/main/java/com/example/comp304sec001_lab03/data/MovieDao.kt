//Xiaoyu Shi and Angelica Cuadrado - Lab 3 assignment

package com.example.comp304sec001_lab03.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY id ASC")
    fun getAll(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE favorite = 1 ORDER BY id ASC")
    fun getFavorites(): Flow<List<Movie>>

    @Query("SELECT EXISTS(SELECT 1 FROM movie WHERE id = :id)")
    suspend fun exists(id: Int): Boolean

    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Movie?
}