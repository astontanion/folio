package com.example.folio.features.photo.model

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("owner")
    val ownerId: String,

    @SerializedName("ownername")
    val ownerName: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("tags")
    val tags: String,

    @SerializedName("url_m")
    val url: String?,

    @SerializedName("width_m")
    val width: Int,

    @SerializedName("height_m")
    val height: Int,

    @SerializedName("server")
    val server: String,

    @SerializedName("farm")
    val farm: String
) {
    fun toModel(): Photo {
        return Photo(
            id = id,
            ownerId = ownerId,
            ownerName = ownerName,
            ownerProfileUrl = "https://farm${farm}.staticflickr.com/${server}/buddyicons/${ownerId}_l.jpg",
            title = title,
            tags = tags.split(""),
            url = url ?: "",
            width = width,
            height = height
        )
    }

}
