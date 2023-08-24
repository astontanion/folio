package com.example.folio.features.photo.model

data class Photo(
    val id: String,
    val title: String,
    val tags: List<String>,
    val url: String,
    val owner: Owner
) {
    companion object {}
}

data class Owner(
    val id: String,
    val username: String,
    val name: String,
    val location: String?,
    val profileUrl: String,
) {
    companion object {}
}
