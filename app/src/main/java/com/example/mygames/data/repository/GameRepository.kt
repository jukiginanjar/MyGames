package com.example.mygames.data.repository

import com.example.mygames.data.model.Game
import com.example.mygames.data.model.Page

interface GameRepository {
    suspend fun getGames(page: Int, searchKey: String?): Page<Game>

    suspend fun findGames(keyword: String, page: Int): Page<Game>

    suspend fun getFavoriteGames(): List<Game>

    suspend fun addFavoriteGame(game: Game)

    suspend fun deleteFavoriteGame(game: Game)
}