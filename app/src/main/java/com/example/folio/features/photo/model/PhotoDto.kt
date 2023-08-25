package com.example.folio.features.photo.model

import com.example.folio.features.user.model.ContentableDto
import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("owner")
    val ownerId: String,

    @SerializedName("ownername")
    val ownerName: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("tags")
    val tags: String,

    @SerializedName("url_m")
    val url: String?,

    @SerializedName("server")
    val server: String,

    @SerializedName("farm")
    val farm: String,
) {

    fun toModel(): Photo {
        return Photo(
            id = id,
            title = title,
            description = null,
            tags = tags.split(" "),
            url = url ?: "",
            dateTaken = null,
            owner = Owner(
                id = ownerId,
                username = ownerName,
                name = "",
                location = "",
                profileUrl = "https://farm${farm}.staticflickr.com/${server}/buddyicons/${ownerId}_l.jpg"
            )
        )
    }
}

data class PhotoDetailWrapperDto(
    @SerializedName("photo")
    val photo: PhotoDetailDto
)

data class PhotoDetailDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: ContentableDto?,

    @SerializedName("description")
    val description: ContentableDto?,

    @SerializedName("tags")
    val tags: TagsContentDto?,

    @SerializedName("secret")
    val secret: String,

    @SerializedName("server")
    val server: String,

    @SerializedName("farm")
    val farm: String,

    @SerializedName("owner")
    val owner: OwnerDto,

    @SerializedName("dates")
    val dateInfo: DateInfo?
) {

    fun toModel(): Photo {
        return Photo(
            id = id,
            title = title?.content.orEmpty(),
            description = description?.content,
            tags = tags?.tag?.map { it.content }?.filter { it.isNotBlank() } ?: emptyList(),
            url = "https://live.staticflickr.com/${server}/${id}_${secret}_b.jpg",
            dateTaken = dateInfo?.taken,
            owner = Owner(
                id = owner.id,
                username = owner.username,
                name = owner.name,
                location = owner.location,
                profileUrl = "https://farm${owner.farm}.staticflickr.com/${owner.server}/buddyicons/${owner.id}_l.jpg"
            )
        )
    }
}

data class TagsContentDto(
    @SerializedName("tag")
    val tag: List<TagContentDto>
)

data class TagContentDto(
    @SerializedName("raw")
    val content: String
)

data class OwnerDto(
    @SerializedName("nsid")
    val id: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("realname")
    val name: String,

    @SerializedName("location")
    val location: String?,

    @SerializedName("iconserver")
    val server: String,

    @SerializedName("iconfarm")
    val farm: Int
) {
    val profileUrl = "https://farm${farm}.staticflickr.com/${server}/buddyicons/${id}_l.jpg"
}

data class DateInfo(
    @SerializedName("taken")
    val taken: String?
)
