package com.example.folio.features.photo.usecase

import com.example.folio.core.di.Io
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.repository.PhotoRepository
import com.example.folio.features.user.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrieveUserPhotosByUsernameUseCaseImpl @Inject constructor(
    @Io
    private val dispatcher: CoroutineDispatcher,
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository
): RetrieveUserPhotosByUsernameUseCase{
    override suspend fun invoke(username: String): Flow<DataResource<PhotosSummary>> {
        return withContext(dispatcher) {
            flow {
                emit(DataResource.Waiting())
                val user = userRepository.retrieveUserByUsername(username)
                val summary = photoRepository.retrieveUserPhotos(userId = user.id)
                emit(DataResource.Success(summary))
            }.catch { ex ->
                emit(DataResource.Failure(ex))
            }
        }
    }
}