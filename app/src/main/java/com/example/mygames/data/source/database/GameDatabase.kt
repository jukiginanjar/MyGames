package com.example.mygames.data.source.database

import androidx.room.*

@Database(entities = [FavoriteGameEntity::class], version = 1)
@TypeConverters(FavoriteGameConverters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun favorite(): FavoriteGameDao
}