package com.example.mygames.data.model

data class GameDetail(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val rating: Double,
    val playtime: Int,
    val thumbnailUrl: String,
    val description: String,
    val developer: String
)
