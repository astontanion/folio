package com.example.folio.features.photo.model

data class Photo(
    val id: String,
    val ownerId: String,
    val ownerName: String,
    val ownerProfileUrl: String,
    val title: String,
    val tags: List<String>,
    val url: String,
    val width: Int,
    val height: Int,
) {
    companion object {}
}
