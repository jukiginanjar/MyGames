package com.example.mygames.data.repository

import android.content.Context
import com.example.mygames.R
import com.example.mygames.data.model.Game
import com.example.mygames.data.model.Page
import com.example.mygames.data.source.database.FavoriteGameDao
import com.example.mygames.data.source.network.GameNetworkApi
import com.example.mygames.data.source.network.GameResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val favoriteGameDao: FavoriteGameDao,
    private val gameNetworkApi: GameNetworkApi,
    @ApplicationContext private val applicationContext: Context,
) : GameRepository {
    override suspend fun getGames(page: Int, searchKey: String?): Page<Game> {
        val result = gameNetworkApi.getGames(
            page = page,
            key = applicationContext.getString(R.string.api_key),
            searchKey = searchKey,
        )
        return Page(
            result.nextRequestUrl?.let { page + 1 },
            result.games.map { it.toGame() }
        )
    }

    override suspend fun findGames(keyword: String, page: Int): Page<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteGames(): List<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavoriteGame(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavoriteGame(game: Game) {
        TODO("Not yet implemented")
    }

    fun GameResponse.Game.toGame(): Game {
        return Game(
            id ?: 0,
            name.orEmpty(),
            released.orEmpty(),
            rating ?: 0.0,
            backgroundImage.orEmpty()
        )
    }
}