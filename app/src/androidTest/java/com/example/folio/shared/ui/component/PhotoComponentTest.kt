package com.example.folio.shared.ui.component

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PhotoComponentTest {

    @get:Rule
    val coroutineTestRule = createComposeRule()

    @Test
    fun has_image() {
        val contentDescription = "The good old image"

        coroutineTestRule.setContent {
            PhotoComponent(
                url = "",
                contentDescription = contentDescription,
                onClick = {}
            )
        }

        with(coroutineTestRule) {
            onNodeWithTag(PHOTO_COMPONENT_TEST_TAG)
                .assertIsDisplayed()
            onNodeWithTag(PHOTO_COMPONENT_TEST_TAG)
                .assert(hasContentDescription(contentDescription))
        }
    }

    @Test
    fun is_clickable() {
        val url = "https://asset.somewebsite.com/images/1234567890"

        var wasClicked = false
        var expectedUrl = ""

        coroutineTestRule.setContent {
            PhotoComponent(
                url = url,
                contentDescription = null,
                onClick = {
                    wasClicked = true
                    expectedUrl = it
                }
            )
        }

        coroutineTestRule.onNodeWithTag(PHOTO_COMPONENT_TEST_TAG).performClick()
        assertTrue(wasClicked)
        assertEquals(url, expectedUrl)
    }
}