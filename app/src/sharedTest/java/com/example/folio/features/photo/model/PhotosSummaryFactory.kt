package com.example.folio.features.photo.model

fun PhotosSummary.Companion.create(
    currentPage: Int = 1,
    totalPages: Int = 100,
    pageSize: Int = 10,
    totalPhotos: Int = 1000,
    photos: List<Photo> = emptyList()
): PhotosSummary {
    return PhotosSummary(currentPage, totalPages, pageSize, totalPhotos, photos)
}