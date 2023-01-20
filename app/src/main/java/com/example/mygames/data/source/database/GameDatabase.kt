package com.example.mygames.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteGameEntity::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun favorite(): FavoriteGameDao
}