package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isIdle
import com.example.folio.core.network.isSuccess
import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.usecase.RetrievePhotoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var retrievePhotoUseCase: RetrievePhotoUseCase
    private lateinit var photoDetailViewModel: PhotoDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        retrievePhotoUseCase = mockk()
        photoDetailViewModel = PhotoDetailViewModel(
            dispatcher = testDispatcher,
            retrievePhotoUseCase = retrievePhotoUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the photo list resource is idle`() = runTest {
        val resource = photoDetailViewModel.uiState.value.photoResource
        assertTrue(resource.isIdle())
    }

    @Test
    fun `given a success response from user's photos, photo resource is success`() = runTest {
        coEvery { retrievePhotoUseCase(any()) } returns flow {
            emit(DataResource.Success(data = Photo.create()))
        }

        photoDetailViewModel.onRefresh("01")

        val resource = photoDetailViewModel.uiState.drop(1)
            .first()
            .photoResource

        assertTrue(resource.isSuccess())
    }

    @Test
    fun `given a failure response from user's photo, photo resource is failure`() = runTest {
        coEvery { retrievePhotoUseCase(any()) } returns flow {
            emit(DataResource.Failure(error = IOException()))
        }

        photoDetailViewModel.onRefresh("01")

        val resource = photoDetailViewModel.uiState.drop(1)
            .first()
            .photoResource

        assertTrue(resource.isFailure())
    }

}