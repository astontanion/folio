package com.example.folio.features.photo.model

data class PhotosSummary(
    val currentPage: Int,
    val totalPages: Int,
    val pageSize: Int,
    val totalPhotos: Int,
    val photos: List<PhotoDto> = emptyList()
)
