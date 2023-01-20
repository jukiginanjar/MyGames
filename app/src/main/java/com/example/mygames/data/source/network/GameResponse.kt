package com.example.mygames.data.source.network

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("previous") val prevRequestUrl: String?,
    @SerializedName("next") val nextRequestUrl: String?,
    @SerializedName("results") val games: List<Game>
) {
    data class Game(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
        @SerializedName("released") val released: String?,
        @SerializedName("background_image") val backgroundImage: String?,
        @SerializedName("rating") val rating: Double?,
    )
}

data class GameDetailResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("released") val released: String?,
    @SerializedName("background_image") val backgroundImage: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("description") val description: String?,
    @SerializedName("playtime") val playtime: Int?,
    @SerializedName("developers") val developers: List<Developer>,
) {
    data class Developer(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
    )
}
