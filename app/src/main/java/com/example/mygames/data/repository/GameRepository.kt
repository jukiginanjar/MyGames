package com.example.mygames.data.repository

import com.example.mygames.data.model.Game
import com.example.mygames.data.model.GameDetail
import com.example.mygames.data.model.Page

interface GameRepository {
    suspend fun getGames(page: Int, searchKey: String?): Page<Game>

    suspend fun getGameDetail(gameId: Int): GameDetail

    suspend fun getFavoriteGames(): List<Game>

    suspend fun addFavoriteGame(game: Game)

    suspend fun deleteFavoriteGame(game: Game)
}