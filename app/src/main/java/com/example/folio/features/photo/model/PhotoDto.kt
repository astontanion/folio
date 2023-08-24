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
            tags = tags.split(" "),
            url = url ?: "",
            owner = Owner(
                id = ownerId,
                username = "",
                name = ownerName,
                location = "",
                profileUrl = "https://farm${farm}.staticflickr.com/${server}/buddyicons/${ownerId}_l.jpg"
            )
        )
    }
}

data class PhotoDetailDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: ContentableDto,

    @SerializedName("description")
    val description: ContentableDto,

    @SerializedName("tags")
    val tags: TagsContentDto,

    @SerializedName("secret")
    val secret: String,

    @SerializedName("server")
    val server: String,

    @SerializedName("farm")
    val farm: String,

    @SerializedName("owner")
    val owner: OwnerDto
) {

    fun toModel(): Photo {
        return Photo(
            id = id,
            title = title.content,
            tags = tags.tag.map { it.content },
            url = "https://live.staticflickr.com/${server}/${id}_${secret}_m.jpg",
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
    val location: String,

    @SerializedName("iconserver")
    val server: String,

    @SerializedName("iconfarm")
    val farm: String
) {
    val profileUrl = "https://farm${farm}.staticflickr.com/${server}/buddyicons/${id}_l.jpg"
}
