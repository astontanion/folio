package com.example.folio.features.photo.usecase

import com.example.folio.core.di.Io
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.repository.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrievePhotoUseCaseImpl @Inject constructor(
    @Io
    private val dispatcher: CoroutineDispatcher,
    private val photoRepository: PhotoRepository,
): RetrievePhotoUseCase {
    override suspend fun invoke(photoId: String): Flow<DataResource<Photo>> {
        return withContext(dispatcher) {
             flow {
                emit(DataResource.Waiting())
                val photo = photoRepository.retrievePhotoWithId(photoId)
                emit(DataResource.Success(photo))
            }.catch { ex ->
                 emit(DataResource.Failure(ex))
            }
        }
    }
}