package com.example.folio.features.photo.repository

import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface PhotoRepository {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: PhotoRepositoryImpl): PhotoRepository
    }

    suspend fun retrievePhotos(): PhotosSummary

    suspend fun searchPhotos(
        tags: String?,
        tagMode: SearchTagMode
    ): PhotosSummary

    suspend fun retrieveUserPhotos(userId: String): PhotosSummary

    suspend fun retrievePhotoWithId(photoId: String): Photo
}