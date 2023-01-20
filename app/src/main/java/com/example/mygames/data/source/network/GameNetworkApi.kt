package com.example.mygames.data.source.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameNetworkApi {
    @GET("api/games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("key") key: String,
        @Query("search") searchKey: String? = null,
    ): GameResponse

    @GET("api/games/{id}")
    suspend fun getGameDetail(@Path("id") id: Int, @Query("key") key: String): GameDetailResponse
}