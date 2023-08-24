package com.example.folio.features.photo.user.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isIdle
import com.example.folio.core.network.isSuccess
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.usecase.RetrieveUserPhotosUseCase
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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var retrieveUserPhotosUseCase: RetrieveUserPhotosUseCase
    private lateinit var userDetailViewModel: UserDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        retrieveUserPhotosUseCase = mockk()
        userDetailViewModel = UserDetailViewModel(
            dispatcher = testDispatcher,
            retrieveUserPhotosUseCase = retrieveUserPhotosUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the photo list resource is idle`() = runTest {
        val resource = userDetailViewModel.uiState.value.photoListSummaryResource
        Assert.assertTrue(resource.isIdle())
    }

    @Test
    fun `given a success response from user's photos, photo resource is success`() = runTest {
        coEvery { retrieveUserPhotosUseCase(any()) } returns flow {
            emit(DataResource.Success(data = PhotosSummary.create()))
        }

        userDetailViewModel.onRefresh("01")

        val resource = userDetailViewModel.uiState.drop(1)
            .first()
            .photoListSummaryResource

        Assert.assertTrue(resource.isSuccess())
    }

    @Test
    fun `given a failure response from user's photo, photo resource is failure`() = runTest {
        coEvery { retrieveUserPhotosUseCase(any()) } returns flow {
            emit(DataResource.Failure(error = IOException()))
        }

        userDetailViewModel.onRefresh("01")

        val resource = userDetailViewModel.uiState.drop(1).first()
            .photoListSummaryResource

        Assert.assertTrue(resource.isFailure())
    }

}