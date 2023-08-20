package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.PhotosSummary
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val service: PhotoService
): PhotoRepository {
    override suspend fun retrieveRecent(): PhotosSummary {
        return service.getRecent().photos.toModel()
    }
}
