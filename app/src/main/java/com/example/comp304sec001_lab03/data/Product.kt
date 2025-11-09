//Xiaoyu Shi and Angelica Cuadrado - Lab 3 assignment

package com.example.comp304sec001_lab03.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey val id: Int,                // 11..99 validated before insert
    val title: String,
    val director: String,
    val price: Double,                      // > 0 validated before insert
    val releaseDateIso: String,             // e.g., "2024-11-12" (ISO-8601) for simplicity
    val durationMinutes: Int,               // e.g., 115
    val genre: String,                      // "Family", "Comedy", etc.
    val favorite: Boolean                   // toggle
)