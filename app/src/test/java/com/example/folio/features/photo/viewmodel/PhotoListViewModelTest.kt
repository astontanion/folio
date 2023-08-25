package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isFailure
import com.example.folio.core.network.isIdle
import com.example.folio.core.network.isSuccess
import com.example.folio.features.photo.model.Owner
import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.usecase.RetrievePhotosUseCase
import com.example.folio.features.photo.usecase.RetrieveUserPhotosByUsernameUseCase
import com.example.folio.features.photo.usecase.SearchPhotosUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var retrievePhotosUseCase: RetrievePhotosUseCase
    private lateinit var searchPhotosUseCase: SearchPhotosUseCase
    private lateinit var retrieveUserPhotosByUsernameUseCase: RetrieveUserPhotosByUsernameUseCase
    private lateinit var photoListViewModel: PhotoListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        retrievePhotosUseCase = mockk()
        searchPhotosUseCase = mockk()
        retrievePhotosUseCase = mockk()
        retrieveUserPhotosByUsernameUseCase = mockk()
        photoListViewModel = PhotoListViewModel(
            dispatcher = testDispatcher,
            retrievePhotosUseCase = retrievePhotosUseCase,
            searchPhotosUseCase = searchPhotosUseCase,
            retrieveUserPhotosByUsernameUseCase = retrieveUserPhotosByUsernameUseCase
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

    @Test
    fun `given a success response from search photos, the uiState's encountered tags list are populated`() = runTest {
        mockOnSearchPhoto()

        val encounteredTags = photoListViewModel.uiState.drop(1)
            .first()
            .encounteredTags

        assertFalse(encounteredTags.isEmpty())
        assertTrue(encounteredTags.containsAll(listOf("alpha", "beta", "gamma", "theta", "zeta")))
    }

    @Test
    fun `given a success response from search photos, the uiState's encountered username list are populated`() = runTest {
        mockOnRetrievePhotos()

        photoListViewModel.onQueryChange("@john")

        mockOnSearchPhoto()

        advanceUntilIdle()

        val encounteredUsernames = photoListViewModel.uiState.value
            .encounteredUsernames

        assertFalse(encounteredUsernames.isEmpty())
        assertTrue(encounteredUsernames.containsAll(listOf("john")))
    }

    @Test
    fun `given a success response from retrieve photos, the uiState's encountered tags list are populated`() = runTest {
        mockOnRetrievePhotos()
        advanceUntilIdle()

        val encounteredTags = photoListViewModel.uiState
            .value
            .encounteredTags

        assertFalse(encounteredTags.isEmpty())
        assertTrue(encounteredTags.containsAll(listOf("alpha", "beta", "gamma", "theta", "zeta")))
    }

    @Test
    fun `given a success response from retrieve photos, the uiState's encountered username list are populated`() = runTest {
        mockOnRetrievePhotos()
        advanceUntilIdle()

        val encounteredUsernames = photoListViewModel.uiState
            .value
            .encounteredUsernames

        assertFalse(encounteredUsernames.isEmpty())
        assertTrue(encounteredUsernames.containsAll(listOf("john", "smith")))
    }

    @Test
    fun `on query change updates the search query, tags and the uiState's filtered tags`() = runTest {
        val query = "my,name,alp"
        photoListViewModel.onQueryChange(query)

        val searchQuery = photoListViewModel.uiState.value
            .searchQuery

        val tags = photoListViewModel.uiState.value.searchTags

        assertEquals(query, searchQuery)
        assertTrue(tags.containsAll(listOf("my", "name", "alp")))
    }

    @Test
    fun `when the query is not empty, filter the encountered tags`() = runTest {
        mockOnRetrievePhotos()

        advanceUntilIdle()

        val query = "my,name,alp"
        photoListViewModel.onQueryChange(query)

        val filteredTags = photoListViewModel.uiState.value
            .filteredTags

        val allMatchQuery = !filteredTags.map { it.contains(query.split(",").last().trim()) }
            .contains(false)

        assertTrue(allMatchQuery)
    }

    @Test
    fun `when the query is not empty, filter the encountered username`() = runTest {
        mockOnSearchPhoto()

        val query = "@Jo"
        photoListViewModel.onQueryChange(query)

        val filteredUsernames = photoListViewModel.uiState.drop(1)
            .first()
            .filteredUsernames

        val allMatchQuery = !filteredUsernames.map { it.lowercase().contains(query.lowercase()) }
            .contains(false)

        assertTrue(allMatchQuery)
    }

    @Test
    fun `when the query is empty, do not filter encountered tags`() = runTest {
        mockOnSearchPhoto()

        val encounteredTags = photoListViewModel.uiState.drop(1)
            .first()
            .encounteredTags

        val query = ""

        photoListViewModel.onQueryChange(query)

        val filteredTags = photoListViewModel.uiState.value
            .encounteredTags

        val tags = photoListViewModel.uiState.value.searchTags

        assertTrue(filteredTags.containsAll(encounteredTags))

        assertTrue(tags.isEmpty())
    }

    @Test
    fun `when the query is empty, do not filter encountered usernames`() = runTest {
        mockOnSearchPhoto()

        val encounteredUsernames = photoListViewModel.uiState.drop(1)
            .first()
            .encounteredUsernames

        val query = "@"

        photoListViewModel.onQueryChange(query)

        val filteredUsernames = photoListViewModel.uiState.value
            .filteredUsernames

        assertTrue(filteredUsernames.containsAll(encounteredUsernames))
    }

    @Test
    fun `on search mode change updates the uiState's search mode`() = runTest {
        photoListViewModel.onSearchModeChange(SearchTagMode.ALL)
        val searchMode = photoListViewModel.uiState.value
            .searchMode
        assertEquals(SearchTagMode.ALL, searchMode)
    }

    @Test
    fun `auto complete the search query to the nearest tag`() = runTest {
        // call to load encountered tags
        mockOnSearchPhoto()

        val query = "alpha, be, gamma"

        // updates the query tags
        photoListViewModel.onQueryChange(query)

        val tag = "beta"
        photoListViewModel.onAutoCompleteTag(tag)

        val newTags = photoListViewModel.uiState.value.searchTags
        val newQuery = photoListViewModel.uiState.value.searchQuery

        assertTrue(newTags.contains(tag))

        assertEquals("alpha, beta, gamma", newQuery)
    }

    @Test
    fun `auto complete the search query to the nearest username`() = runTest {
        // call to load encountered tags
        mockOnRetrievePhotos()

        val query = "@Jo"

        // updates the query tags
        photoListViewModel.onQueryChange(query)

        val username = "John"
        photoListViewModel.onAutoCompleteTag(username)


        val newQuery = photoListViewModel.uiState.value.searchQuery

        assertEquals("@John", newQuery)
    }

    private suspend fun mockOnSearchPhoto() {
        coEvery {
            searchPhotosUseCase(
                tags = any(),
                tagMode = any()
            )
        } returns flow {
            emit(
                DataResource.Success(
                    data = PhotosSummary.create(
                        photos = listOf(
                            Photo.create(
                                owner = Owner.create(username = "john"),
                                tags = listOf("alpha", "beta", "gamma")
                            ),
                            Photo.create(
                                owner = Owner.create(username = "smith"),
                                tags = listOf("alpha", "gamma", "theta", "zeta")
                            ),
                        )
                    )
                )
            )
        }

        coEvery {
            retrieveUserPhotosByUsernameUseCase(any())
        } returns flow {
            DataResource.Success(
                data = PhotosSummary.create(
                    photos = listOf(
                        Photo.create(
                            owner = Owner.create(username = "john"),
                            tags = listOf("alpha", "beta", "gamma")
                        ),
                        Photo.create(
                            owner = Owner.create(username = "john"),
                            tags = listOf("alpha", "gamma", "theta", "zeta")
                        ),
                    )
                )
            )
        }

        photoListViewModel.onSearch()
    }

    private suspend fun mockOnRetrievePhotos() {
        coEvery {
            retrievePhotosUseCase()
        } returns flow {
            emit(
                DataResource.Success(
                    data = PhotosSummary.create(
                        photos = listOf(
                            Photo.create(
                                owner = Owner.create(username = "john"),
                                tags = listOf("alpha", "beta", "gamma")
                            ),
                            Photo.create(
                                owner = Owner.create(username = "smith"),
                                tags = listOf("alpha", "gamma", "theta", "zeta")
                            ),
                        )
                    )
                )
            )
        }

        photoListViewModel.onRefresh()
    }
}