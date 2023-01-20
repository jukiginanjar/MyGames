package com.example.mygames.data.model

import java.util.*

data class GameDetail(
    val title: String,
    val releaseDate: Date,
    val rating: Double,
    val thumbnailUrl: String,
    val description: String,
    val developer: String
)
