package com.example.folio.features.photo.usecase

import com.example.folio.core.di.Io
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.repository.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrieveRecentPhotoUseCaseImpl @Inject constructor(
    @Io
    private val dispatcher: CoroutineDispatcher,
    private val photoRepository: PhotoRepository
): RetrieveRecentPhotoUseCase {
    override suspend fun invoke(): Flow<DataResource<PhotosSummary>> {
        return withContext(dispatcher) {
             flow {
                emit(DataResource.Waiting())
                val summary = photoRepository.retrieveRecent()
                emit(DataResource.Success(summary))
            }.catch { ex ->
                 emit(DataResource.Failure(ex))
            }
        }
    }
}