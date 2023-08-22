package com.example.folio.features.photo.repository

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
        query: String?,
        tagMode: SearchTagMode?
    ): PhotosSummary
}