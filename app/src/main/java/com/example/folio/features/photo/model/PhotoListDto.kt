package com.example.folio.features.photo.model

import com.google.gson.annotations.SerializedName

data class PhotoListDto(
    @SerializedName("photos")
    val photos: PhotosSummaryDto,

    @SerializedName("stat")
    val stat: String
)

data class PhotosSummaryDto(
    @SerializedName("page")
    val currentPage: Int,

    @SerializedName("pages")
    val totalPages: Int,

    @SerializedName("perpage")
    val pageSize: Int,

    @SerializedName("total")
    val totalPhotos: Int,

    @SerializedName("photo")
    val photos: List<PhotoDto> = emptyList()
) {
    fun toModel(): PhotosSummary {
        return PhotosSummary(
            currentPage = currentPage,
            totalPages = totalPages,
            pageSize = pageSize,
            totalPhotos = totalPhotos,
            photos = photos.filter { it.url != null }.map { it.toModel() }
        )
    }
}
