package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import com.example.folio.features.photo.user.repository.UserRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val service: PhotoService,
): PhotoRepository {
    override suspend fun retrievePhotos(): PhotosSummary {
        return service.retrievePhotos().photos.toModel()
    }

    override suspend fun searchPhotos(
        tags: String?,
        query: String?,
        tagMode: SearchTagMode?
    ): PhotosSummary {
        return service.searchPhotos(tags, query, tagMode).photos.toModel()
    }
}
