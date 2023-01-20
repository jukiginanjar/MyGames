package com.example.mygames.data.model

data class Page<T>(val nextPage: Int?, val items: List<T>)

data class Game(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val rating: Double,
    val thumbnailUrl: String
)
