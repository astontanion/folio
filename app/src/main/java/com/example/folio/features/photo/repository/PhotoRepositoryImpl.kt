package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.PhotosSummary
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val service: PhotoService
): PhotoRepository {
    override suspend fun retrievePhotos(): PhotosSummary {
        return service.retrievePhotos().photos.toModel()
    }
}
