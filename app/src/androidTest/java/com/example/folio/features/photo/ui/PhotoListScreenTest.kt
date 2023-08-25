package com.example.folio.features.photo.ui

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.folio.R
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.create
import com.example.folio.features.photo.viewmodel.PhotoListUiState
import com.example.folio.features.photo.viewmodel.create
import com.example.folio.shared.ui.theme.FolioTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PhotoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun the_search_bar_is_displayed() {
        composeTestRule.setContent {
            FolioTheme {
                PhotoListScreen(
                    uiState = PhotoListUiState.create(),
                    onRefresh = {},
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(SEARCH_BAR_TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun when_retrieving_photos_fails_show_an_error() {
        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Failure(IOException())
        )

        var errorMessage = ""
        var errorButtonText = ""

        composeTestRule.setContent {
            errorMessage = stringResource(id = R.string.unable_to_fetch_photos)
            errorButtonText = stringResource(id = R.string.button_try_again)

            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }

        }

        with(composeTestRule) {
            onNodeWithText(errorMessage)
                .assertIsDisplayed()

            onNodeWithText(errorButtonText)
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_clicking_on_retry_button_on_refresh_is_called() {
        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Failure(IOException())
        )

        var hasClickedOnRefresh = false

        var errorButtonText = ""

        composeTestRule.setContent {
            errorButtonText = stringResource(id = R.string.button_try_again)

            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {
                        hasClickedOnRefresh = true
                    },
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(errorButtonText)
            .performClick()

        assertTrue(hasClickedOnRefresh)
    }

    @Test
    fun when_no_photo_is_found_show_message() {
        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create()
            )
        )

        var photoNotFoundText = ""

        var getRecentButtonTest = ""

        composeTestRule.setContent {
            photoNotFoundText = stringResource(id = R.string.photos_not_found)
            getRecentButtonTest = stringResource(id = R.string.button_get_recent)

            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        with (composeTestRule) {
            onNodeWithText(photoNotFoundText)
                .assertIsDisplayed()

            onNodeWithText(getRecentButtonTest)
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_no_photo_is_found_clicking_on_get_recent_calls_on_refresh() {
        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create()
            )
        )

        var hasClickedOnGetRecentButton = false

        var getRecentButtonTest = ""

        composeTestRule.setContent {
            getRecentButtonTest = stringResource(id = R.string.button_get_recent)

            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {
                        hasClickedOnGetRecentButton = true
                    },
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(getRecentButtonTest)
            .performClick()

        assertTrue(hasClickedOnGetRecentButton)
    }

    @Test
    fun when_photos_are_not_empty_display_a_list_of_images() {
        val photos = listOf(
            Photo.create(id = "01"),
            Photo.create(id = "02"),
            Photo.create(id = "03"),
        )

        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create(
                    photos = photos
                )
            )
        )

        composeTestRule.setContent {
            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onAllNodesWithText(photos.first().title)
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun when_searching_for_tags_on_query_changed_is_called() {
        val photos = listOf(
            Photo.create(id = "01"),
            Photo.create(id = "02"),
            Photo.create(id = "03"),
        )

        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create(
                    photos = photos
                )
            )
        )

        var searchedTag = ""

        composeTestRule.setContent {
            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onQueryChange = {searchedTag = it },
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = {},
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        val tagInput = "al"

        val searchBar = composeTestRule.onNodeWithTag(SEARCH_BAR_TEST_TAG)
        searchBar.assertIsDisplayed()

        val testInputNode = searchBar.onChildren().filter(hasSetTextAction()).onFirst()
        testInputNode.performTextInput(tagInput)

        assertEquals(tagInput, searchedTag)
    }

    @Test
    fun when_clicking_on_owner_info_from_photo_post_on_user_click_is_called() {
        val photo = Photo.create(id = "01")

        val uiState = PhotoListUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create(
                    photos = listOf(photo)
                )
            )
        )

        var hasClickedOnOwnerInfo = false

        composeTestRule.setContent {
            FolioTheme {
                PhotoListScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onQueryChange = {},
                    onSearch = {},
                    onSearchModeChange = {},
                    onAutoComplete = {},
                    onUserClick = { hasClickedOnOwnerInfo = true },
                    onTagClick = {},
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(photo.owner.username)
            .performClick()

        assertTrue(hasClickedOnOwnerInfo)
    }
}