package com.example.mygames.data.repository

import android.content.Context
import com.example.mygames.R
import com.example.mygames.data.model.Game
import com.example.mygames.data.model.GameDetail
import com.example.mygames.data.model.Page
import com.example.mygames.data.source.database.FavoriteGameDao
import com.example.mygames.data.source.database.FavoriteGameEntity
import com.example.mygames.data.source.network.GameNetworkApi
import com.example.mygames.data.source.network.GameResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun getGameDetail(gameId: Int): GameDetail {
        val result =
            gameNetworkApi.getGameDetail(gameId, applicationContext.getString(R.string.api_key))
        return result.run {
            GameDetail(
                id = id ?: 0,
                title = name.orEmpty(),
                releaseDate = released.orEmpty(),
                rating = rating ?: 0.0,
                playtime = playtime ?: 0,
                thumbnailUrl = backgroundImage.orEmpty(),
                description = description.orEmpty(),
                developer = developers.firstOrNull()?.name.orEmpty()
            )
        }
    }

    override fun getFavoriteGames(): Flow<List<Game>> {
        return favoriteGameDao.getFavoriteGames().map { entities ->
            entities.map { entity ->
                Game(
                    id = entity.id,
                    title = entity.title,
                    releaseDate = entity.releaseDate,
                    rating = entity.rating,
                    thumbnailUrl = entity.thumbnailUrl
                )
            }
        }
    }

    override suspend fun isFavoriteGame(gameId: Int): Boolean {
        return favoriteGameDao.isFavoriteGame(gameId)
    }

    override suspend fun addFavoriteGame(game: GameDetail) {
        favoriteGameDao.addFavoriteGame(
            FavoriteGameEntity(
                id = game.id,
                title = game.title,
                releaseDate = game.releaseDate,
                rating = game.rating,
                thumbnailUrl = game.thumbnailUrl
            )
        )
    }

    override suspend fun deleteFavoriteGame(gameId: Int) {
        favoriteGameDao.deleteFavoriteGame(gameId)
    }

    private fun GameResponse.Game.toGame(): Game {
        return Game(
            id ?: 0,
            name.orEmpty(),
            released.orEmpty(),
            rating ?: 0.0,
            backgroundImage.orEmpty()
        )
    }
}