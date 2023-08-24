package com.example.folio.features.photo.user.model

import com.example.folio.core.extension.emptyIfNull
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("person")
    val person: Person
) {
    fun toModel(): User {
        return User(
            id = person.id,
            username = person.username?.content.emptyIfNull(),
            fullName = person.fullName?.content.emptyIfNull(),
            location = person.location?.content.emptyIfNull(),
            description = person.description?.content.emptyIfNull(),
            photosUrl = person.photosUrl?.content.emptyIfNull(),
            profileUrl = person.photosUrl?.content.emptyIfNull()
        )
    }
}

data class Person(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val username: Contentable?,

    @SerializedName("realname")
    val fullName: Contentable?,

    @SerializedName("location")
    val location: Contentable?,

    @SerializedName("description")
    val description: Contentable?,

    @SerializedName("photosurl")
    val photosUrl: Contentable?,

    @SerializedName("profileurl")
    val profileUrl: Contentable?
)

data class Contentable(
    @SerializedName("_content")
    val content: String? = ""
)