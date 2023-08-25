package com.example.folio.features.photo.usecase

import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isSuccess
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.repository.PhotoRepository
import com.example.folio.features.user.model.User
import com.example.folio.features.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RetrieveUserPhotosByUsernameUseCaseImplTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var photoRepository: PhotoRepository
    private lateinit var userRepository: UserRepository
    private lateinit var retrieveUserPhotosByUsernameUseCase: RetrieveUserPhotosByUsernameUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        photoRepository = mockk()
        userRepository = mockk()
        retrieveUserPhotosByUsernameUseCase = RetrieveUserPhotosByUsernameUseCaseImpl(
            dispatcher = testDispatcher,
            photoRepository = photoRepository,
            userRepository = userRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test that the first emission is waiting`() = runTest {
        val result = retrieveUserPhotosByUsernameUseCase("username").first()
        assertTrue(result.isWaiting())
    }

    @Test
    fun `when fails to retrieve user returns a failure data resource`() = runTest {
        coEvery { userRepository.retrieveUserByUsername(any()) } throws Exception()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveUserPhotosByUsernameUseCase("username").drop(1).first()

        assertTrue(result.isFailure())
    }

    @Test
    fun `when fails to retrieve photo returns a failure data resource`() = runTest {

        coEvery { userRepository.retrieveUserByUsername(any()) } returns User(
            id = "01",
            username = "username"
        )

        coEvery { photoRepository.retrieveUserPhotos("username") } throws Exception()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveUserPhotosByUsernameUseCase("username").drop(1).first()

        assertTrue(result.isFailure())
    }

    @Test
    fun `when the api succeed returns a success data resource`() = runTest {
        coEvery { userRepository.retrieveUserByUsername("username") } returns User(
            id = "01",
            username = "username"
        )
        coEvery { photoRepository.retrieveUserPhotos("01") } returns PhotosSummary.create()

        // note that we are dropping 1 because the first emission is always waiting.
        val result = retrieveUserPhotosByUsernameUseCase("username").drop(1).first()

        print(result)

        assertTrue(result.isSuccess())
    }
}