package com.example.folio.features.photo.usecase

import com.example.folio.core.di.Io
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import com.example.folio.features.photo.repository.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchPhotosUseCaseImpl @Inject constructor(
    @Io
    private val dispatcher: CoroutineDispatcher,
    private val photoRepository: PhotoRepository,
): SearchPhotosUseCase {
    override suspend fun invoke(
        tags: List<String>,
        tagMode: SearchTagMode
    ): Flow<DataResource<PhotosSummary>> {
        return withContext(dispatcher) {
             flow {
                emit(DataResource.Waiting())
                val summary = photoRepository.searchPhotos(
                    tags = tags.joinToString(","),
                    tagMode = tagMode
                )
                emit(DataResource.Success(summary))
            }.catch { ex ->
                 emit(DataResource.Failure(ex))
            }
        }
    }
}