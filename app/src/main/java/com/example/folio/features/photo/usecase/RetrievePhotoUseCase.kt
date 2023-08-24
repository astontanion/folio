package com.example.folio.features.photo.usecase

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.Photo
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface RetrievePhotoUseCase {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: RetrievePhotoUseCaseImpl): RetrievePhotoUseCase
    }

    suspend operator fun invoke(photoId: String): Flow<DataResource<Photo>>
}