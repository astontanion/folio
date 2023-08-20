package com.example.folio.features.photo.usecase

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.repository.PhotoRepository
import io.mockk.coEvery
import io.mockk.every
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
class RetrieveRecentPhotoUseCaseImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var photoRepository: PhotoRepository
    private lateinit var retrieveRecentPhotoUseCase: RetrieveRecentPhotoUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        photoRepository = mockk()
        retrieveRecentPhotoUseCase = RetrieveRecentPhotoUseCaseImpl(
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
        val result = retrieveRecentPhotoUseCase().first()
        assertTrue(result.isWaiting())
    }

    @Test
    fun `when the api fails returns a failure data resource`() = runTest {
        coEvery { photoRepository.retrieveRecent() } throws Exception()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveRecentPhotoUseCase().drop(1).first()

        assertTrue(result.isFailure())
    }

    @Test
    fun `when the api succeed returns a success data resource`() = runTest {
        coEvery { photoRepository.retrieveRecent() } returns PhotosSummary(
            currentPage = 0,
            totalPages = 1,
            pageSize = 2,
            totalPhotos = 3,
            photos = emptyList()
        )

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveRecentPhotoUseCase().drop(1).first()

        assertTrue(result.isSuccess())

        val data = (result as DataResource.Success).data

        assertEquals(2, data.pageSize)
    }
}