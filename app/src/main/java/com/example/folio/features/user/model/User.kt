package com.example.folio.features.photo.user.model

data class User(
    val id: String,
    val username: String,
    val fullName: String,
    val location: String,
    val description: String,
    val photosUrl: String,
    val profileUrl: String
)
