package com.example.mygames.data.di

import android.content.Context
import androidx.room.Room
import com.example.mygames.data.source.database.FavoriteGameDao
import com.example.mygames.data.source.database.GameDatabase
import com.example.mygames.data.source.network.GameNetworkApi
import com.example.mygames.data.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
    ): GameDatabase = Room.databaseBuilder(
        context,
        GameDatabase::class.java,
        "game-database"
    ).build()

    @Provides
    @Singleton
    fun providesFavoriteGameDao(
        gameDatabase: GameDatabase
    ): FavoriteGameDao = gameDatabase.favorite()

    @Provides
    @Singleton
    fun provideNetworkApi(): GameNetworkApi =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideDispatcherProvider() = DispatcherProvider()

    companion object {
        const val NETWORK_REQUEST_TIMEOUT_SECONDS = 15L
        const val BASE_URL = "https://api.rawg.io/"
    }
}