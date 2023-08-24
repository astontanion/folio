package com.example.folio.features.user.ui

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.folio.R
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.Photo
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.create
import com.example.folio.features.user.viewmodel.UserDetailUiState
import com.example.folio.features.user.viewmodel.create
import com.example.folio.shared.ui.theme.FolioTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class UserDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_the_back_navigation_is_clicked_on_back_pressed_is_called() {
        val uiState = UserDetailUiState.create()

        var backButtonContentDescription = ""
        var hasClickOnBack = false

        composeTestRule.setContent {
            backButtonContentDescription = stringResource(
                id = R.string.content_description_go_back
            )

            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = { hasClickOnBack = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(backButtonContentDescription)
            .performClick()

        assertTrue(hasClickOnBack)
    }

    @Test
    fun when_user_photo_call_is_not_successful_the_user_info_component_is_not_visible() {
        val uiState = UserDetailUiState.create()

        composeTestRule.setContent {
            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(USER_INFO_COMPONENT_TEST_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun when_user_photo_call_is_successful_but_with_no_photo_the_user_info_component_is_not_visible() {
        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create()
            )
        )

        composeTestRule.setContent {
            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(USER_INFO_COMPONENT_TEST_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun when_user_photo_call_is_successful_with_photos_the_user_info_component_is_visible() {
        val photo = Photo.create()

        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create(
                    photos = listOf(photo)
                )
            )
        )

        composeTestRule.setContent {
            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = {}
                )
            }
        }

        with (composeTestRule) {
            onNodeWithTag(USER_INFO_COMPONENT_TEST_TAG)
                .assertIsDisplayed()

            onNodeWithText(photo.ownerName)
                .assertIsDisplayed()
        }
    }

    @Test
    fun when_retrieving_photos_fails_show_an_error() {
        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Failure(IOException())
        )

        var errorMessage = ""
        var errorButtonText = ""

        composeTestRule.setContent {
            errorMessage = stringResource(id = R.string.unable_to_fetch_user_photos)
            errorButtonText = stringResource(id = R.string.button_try_again)

            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = {}
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
        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Failure(IOException())
        )

        var hasClickedOnRefresh = false

        var errorButtonText = ""

        composeTestRule.setContent {
            errorButtonText = stringResource(id = R.string.button_try_again)

            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = { hasClickedOnRefresh = true },
                    onBackPress = {}
                )
            }
        }

        composeTestRule.onNodeWithText(errorButtonText)
            .performClick()

        assertTrue(hasClickedOnRefresh)
    }

    @Test
    fun when_no_photo_is_found_show_message() {
        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create()
            )
        )

        var photoNotFoundText = ""

        var getRecentButtonTest = ""

        composeTestRule.setContent {
            photoNotFoundText = stringResource(id = R.string.user_photos_not_found)
            getRecentButtonTest = stringResource(id = R.string.button_try_again)

            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = {}
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
    fun when_no_photo_is_found_clicking_on_retry_calls_on_refresh() {
        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create()
            )
        )

        var hasClickedOnRetry = false

        var tryAgainButton = ""

        composeTestRule.setContent {
            tryAgainButton = stringResource(id = R.string.button_try_again)

            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = { hasClickedOnRetry = true },
                    onBackPress = {}
                )
            }
        }

        composeTestRule.onNodeWithText(tryAgainButton)
            .performClick()

        assertTrue(hasClickedOnRetry)
    }

    @Test
    fun when_photos_are_not_empty_display_a_list_of_images() {
        val photos = listOf(
            Photo.create(id = "01"),
            Photo.create(id = "02"),
            Photo.create(id = "03"),
        )

        val uiState = UserDetailUiState.create(
            photoListSummaryResource = DataResource.Success(
                PhotosSummary.create(
                    photos = photos
                )
            )
        )

        composeTestRule.setContent {
            FolioTheme {
                UserDetailScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onBackPress = {}
                )
            }
        }

        composeTestRule.onAllNodesWithContentDescription(photos.first().title)
            .onFirst()
            .assertIsDisplayed()
    }
}