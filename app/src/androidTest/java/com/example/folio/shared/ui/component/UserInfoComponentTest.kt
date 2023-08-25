package com.example.folio.shared.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.folio.shared.ui.theme.FolioTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserInfoComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun has_image() {
        composeTestRule.setContent {
            FolioTheme {
                UserInfoComponent(
                    url = "",
                    username = "",
                    location = "",
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(USER_INFO_IMAGE_TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun has_username() {
        val username = "johnsmith"
        composeTestRule.setContent {
            FolioTheme {
                UserInfoComponent(
                    url = "",
                    username = username,
                    location = "",
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(username)
            .assertIsDisplayed()
    }

    @Test
    fun has_location() {
        val location = "england"
        composeTestRule.setContent {
            FolioTheme {
                UserInfoComponent(
                    url = "",
                    username = "",
                    location = location,
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(location)
            .assertIsDisplayed()
    }

    @Test
    fun does_not_have_location() {
        composeTestRule.setContent {
            FolioTheme {
                UserInfoComponent(
                    url = "",
                    username = "",
                    location = null,
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(USER_INFO_LOCATION_TEST_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun clicking_on_image_calls_on_click() {
        var wasClicked = false

        composeTestRule.setContent {
            FolioTheme {
                UserInfoComponent(
                    url = "",
                    username = "",
                    location = null,
                    onClick = { wasClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithTag(USER_INFO_IMAGE_TEST_TAG)
            .performClick()

        assertTrue(wasClicked)
    }

    @Test
    fun clicking_on_username_calls_on_click() {
        var wasClicked = false
        val username = "johnsmith"

        composeTestRule.setContent {
            FolioTheme {
                UserInfoComponent(
                    url = "",
                    username = username,
                    location = null,
                    onClick = { wasClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText(username)
            .performClick()

        assertTrue(wasClicked)
    }
}