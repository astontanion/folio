package com.example.folio.features.photo.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: String,
    val owner: String,
    val ownerName: String,
    val title: String,
    val tags: List<String>,
    val url: String,
    val width: Int,
    val height: Int
)