package com.example.folio.features.photo.usecase

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface RetrieveUserPhotosByUsernameUseCase {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: RetrieveUserPhotosByUsernameUseCaseImpl): RetrieveUserPhotosByUsernameUseCase
    }

    suspend operator fun invoke(username: String): Flow<DataResource<PhotosSummary>>
}