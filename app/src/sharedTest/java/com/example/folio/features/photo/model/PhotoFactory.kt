package com.example.folio.features.photo.model

fun Photo.Companion.create(
    id: String = "01",
    title: String = "",
    tags: List<String> = emptyList(),
    url: String = "",
    owner: Owner = Owner.create()
): Photo {
    return Photo(
        id = id,
        title = title,
        tags = tags,
        url = url,
        owner = owner
    )
}

fun Owner.Companion.create(
    id: String = "01",
    username: String = "johnsmith",
    name: String = "John Smith",
    location: String = "",
    profileUrl: String = "",
): Owner {
    return Owner(
        id = id,
        username = username,
        name = name,
        location = location,
        profileUrl = profileUrl
    )
}