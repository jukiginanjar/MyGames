package com.example.mygames.data.repository

import com.example.mygames.data.model.Game
import com.example.mygames.data.model.GameDetail
import com.example.mygames.data.model.Page
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getGames(page: Int, searchKey: String?): Page<Game>

    suspend fun getGameDetail(gameId: Int): GameDetail

    fun getFavoriteGames(): Flow<List<Game>>

    suspend fun isFavoriteGame(gameId: Int): Boolean

    suspend fun addFavoriteGame(game: GameDetail)

    suspend fun deleteFavoriteGame(gameId: Int)
}