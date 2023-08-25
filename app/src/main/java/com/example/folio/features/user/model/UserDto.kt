package com.example.folio.features.user.model

import com.google.gson.annotations.SerializedName

data class UserWrapperDto(
    @SerializedName("user")
    val user: UserDto
) {
    fun toModel(): User {
        return User(
            id = user.id,
            username = user.username?.content.orEmpty(),
        )
    }
}

data class UserDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val username: ContentableDto?,
)

data class ContentableDto(
    @SerializedName("_content")
    val content: String = ""
)