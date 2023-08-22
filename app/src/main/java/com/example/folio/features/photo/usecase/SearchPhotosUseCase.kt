package com.example.folio.features.photo.usecase

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface SearchPhotosUseCase {

    @dagger.Module
    @InstallIn(SingletonComponent::class)
    interface Module {
        @Binds
        @Singleton
        fun bind(impl: SearchPhotosUseCaseImpl): SearchPhotosUseCase
    }

    suspend operator fun invoke(
        tags: List<String>,
        query: String?,
        tagMode: SearchTagMode
    ): Flow<DataResource<PhotosSummary>>
}