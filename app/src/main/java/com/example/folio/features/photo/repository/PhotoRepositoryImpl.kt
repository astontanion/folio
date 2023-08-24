package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val service: PhotoService,
): PhotoRepository {
    override suspend fun retrievePhotos(): PhotosSummary {
        return service.retrievePhotos().photos.toModel()
    }

    override suspend fun searchPhotos(
        tags: String?,
        tagMode: SearchTagMode
    ): PhotosSummary {
        return service.searchPhotos(tags, tagMode.name.lowercase()).photos.toModel()
    }

    override suspend fun retrieveUserPhotos(userId: String): PhotosSummary {
        return service.retrieveUserPhotos(userId).photos.toModel()
    }

    override suspend fun retrievePhotoWithId(photoId: String): Photo {
        return service.retrievePhotoWithId(photoId).photo.toModel()
    }
}
