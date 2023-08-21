package com.example.folio.features.photo.usecase

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface RetrievePhotosUseCase {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: RetrievePhotosUseCaseImpl): RetrievePhotosUseCase
    }

    suspend operator fun invoke(): Flow<DataResource<PhotosSummary>>
}