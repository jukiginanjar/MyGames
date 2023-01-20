package com.example.mygames.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGameDao {
    @Query("SELECT * FROM favorite_games")
    fun getFavoriteGames(): Flow<List<FavoriteGameEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_games WHERE id = :gameId)")
    suspend fun isFavoriteGame(gameId: Int?): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteGame(gameEntity: FavoriteGameEntity)

    @Query("DELETE FROM favorite_games WHERE id = :gameId")
    suspend fun deleteFavoriteGame(gameId: Int)
}