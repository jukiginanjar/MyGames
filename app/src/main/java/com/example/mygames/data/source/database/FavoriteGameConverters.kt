package com.example.mygames.data.source.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class FavoriteGameConverters {
    private val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun fromFormattedDate(value: String?): Date? {
        return try {
            value?.let { df.parse(it) }
        } catch (e: Throwable) {
            null
        }
    }

    @TypeConverter
    fun toDate(date: Date?): String? {
        return date?.let { df.format(it) }
    }
}