package com.example.folio.features.photo.usecase

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isSuccess
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.repository.PhotoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RetrieveUserPhotosUseCaseImplTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var photoRepository: PhotoRepository
    private lateinit var retrieveUserPhotosUseCase: RetrieveUserPhotosUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        photoRepository = mockk()
        retrieveUserPhotosUseCase = RetrieveUserPhotosUseCaseImpl(
            dispatcher = testDispatcher,
            photoRepository = photoRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test that the first emission is waiting`() = runTest {
        val result = retrieveUserPhotosUseCase("01").first()
        Assert.assertTrue(result.isWaiting())
    }

    @Test
    fun `when the api fails returns a failure data resource`() = runTest {
        coEvery { photoRepository.retrieveUserPhotos(any()) } throws Exception()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveUserPhotosUseCase("01").drop(1).first()

        Assert.assertTrue(result.isFailure())
    }

    @Test
    fun `when the api succeed returns a success data resource`() = runTest {
        coEvery { photoRepository.retrieveUserPhotos(any()) } returns PhotosSummary.create(
            pageSize = 100
        )

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveUserPhotosUseCase("01").drop(1).first()

        Assert.assertTrue(result.isSuccess())

        val data = (result as DataResource.Success).data

        Assert.assertEquals(100, data.pageSize)
    }
}