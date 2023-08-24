package com.example.folio.features.photo.usecase

import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isSuccess
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.Photo
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RetrievePhotoUseCaseImplTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var photoRepository: PhotoRepository
    private lateinit var retrievePhotoUseCase: RetrievePhotoUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        photoRepository = mockk()
        retrievePhotoUseCase = RetrievePhotoUseCaseImpl(
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
        val result = retrievePhotoUseCase("01").first()
        assertTrue(result.isWaiting())
    }

    @Test
    fun `when the api fails returns a failure data resource`() = runTest {
        coEvery { photoRepository.retrievePhotoWithId(any()) } throws Exception()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrievePhotoUseCase("01").drop(1).first()

        assertTrue(result.isFailure())
    }

    @Test
    fun `when the api succeed returns a success data resource`() = runTest {
        coEvery { photoRepository.retrievePhotoWithId(any()) } returns Photo.create()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrievePhotoUseCase("01").drop(1).first()

        assertTrue(result.isSuccess())
    }
}