package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isIdle
import com.example.folio.core.network.isSuccess
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.usecase.RetrievePhotosUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var retrievePhotosUseCase: RetrievePhotosUseCase
    private lateinit var photoListViewModel: PhotoListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        retrievePhotosUseCase = mockk()
        photoListViewModel = PhotoListViewModel(
            dispatcher = testDispatcher,
            retrievePhotosUseCase = retrievePhotosUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the photo list resource is idle`() = runTest {
        val resource = photoListViewModel.uiState.value.photoListSummaryResource
        assertTrue(resource.isIdle())
    }

    @Test
    fun `given a success response from recent photo, photo resource is success`() = runTest {
        coEvery { retrievePhotosUseCase() } returns flow {
            emit(DataResource.Success(data = PhotosSummary.create()))
        }

        photoListViewModel.onRefresh()

        val resource = photoListViewModel.uiState.drop(1)
            .first()
            .photoListSummaryResource

        assertTrue(resource.isSuccess())
    }

    @Test
    fun `given a failure response from recent photo, photo resource is failure`() = runTest {
        coEvery { retrievePhotosUseCase() } returns flow {
            emit(DataResource.Failure(error = IOException()))
        }

        photoListViewModel.onRefresh()

        val resource = photoListViewModel.uiState.drop(1).first()
            .photoListSummaryResource

        assertTrue(resource.isFailure())
    }
}