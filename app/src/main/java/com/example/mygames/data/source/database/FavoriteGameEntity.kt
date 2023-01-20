package com.example.mygames.data.source.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "favorite_games")
data class FavoriteGameEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "release_date") val releaseDate: Date,
    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String
)