package com.example.folio.features.photo.model

fun Photo.Companion.create(
    id: String = "01",
    ownerId: String = "01",
    ownerName: String = "John Smith",
    ownerProfileUrl: String = "",
    title: String = "",
    tags: List<String> = emptyList(),
    url: String = "",
    width: Int = 100,
    height: Int = 100,
): Photo {
    return Photo(
        id = id,
        ownerId = ownerId,
        ownerName = ownerName,
        ownerProfileUrl = ownerProfileUrl,
        title = title,
        tags = tags,
        url = url,
        width = width,
        height = height
    )
}