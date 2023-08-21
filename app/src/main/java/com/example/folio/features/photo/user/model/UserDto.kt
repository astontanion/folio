package com.example.folio.features.photo.user.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val username: Contentable,

    @SerializedName("realname")
    val fullName: Contentable,

    @SerializedName("location")
    val location: Contentable,

    @SerializedName("description")
    val description: Contentable,

    @SerializedName("photosurl")
    val photosUrl: Contentable,

    @SerializedName("profileurl")
    val profileUrl: Contentable
) {
    fun toModel(): User {
        return User(
            id = id,
            username = username.content,
            fullName = fullName.content,
            location = location.content,
            description = description.content,
            photosUrl = photosUrl.content,
            profileUrl = photosUrl.content
        )
    }
}

data class Contentable(
    @SerializedName("_content")
    val content: String
)