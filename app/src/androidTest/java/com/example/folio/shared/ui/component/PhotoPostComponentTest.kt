package com.example.folio.shared.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PhotoPostComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun has_owner_detail() {
        val ownerName = "John Smith"
        val ownerUrl = "ownerUrl"
        val photoUrl = "photoUrl"
        val title = "My cool image"

        composeTestRule.setContent {
            PhotoPostComponent(
                photoUrl = photoUrl,
                ownerUrl = ownerUrl,
                username = ownerName,
                title = title,
                tags = emptyList(),
                onOwnerClick = {},
                onImageClick = {},
                onTagClick = {},
            )
        }

        with(composeTestRule) {
            onNodeWithTag(USER_INFO_IMAGE_TEST_TAG).assertIsDisplayed()
            onNodeWithText(ownerName).assertIsDisplayed()
            onNodeWithTag(PHOTO_COMPONENT_TEST_TAG).assertIsDisplayed()
            onNodeWithText(title).assertIsDisplayed()
        }
    }

    @Test
    fun clicking_on_owner_detail_triggers_a_callback() {
        val ownerName = "John Smith"
        val ownerUrl = "ownerUrl"
        val photoUrl = "photoUrl"
        val title = "My cool image"

        var hasClickedOnOwner = false

        composeTestRule.setContent {
            PhotoPostComponent(
                photoUrl = photoUrl,
                ownerUrl = ownerUrl,
                username = ownerName,
                title = title,
                tags = emptyList(),
                onOwnerClick = {
                    hasClickedOnOwner = true
                },
                onImageClick = {},
                onTagClick = {}
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("MYTAG")

        with(composeTestRule) {
            onNodeWithTag(USER_INFO_IMAGE_TEST_TAG, useUnmergedTree = true).performClick()

            assertTrue(hasClickedOnOwner)

            hasClickedOnOwner = false

            onNodeWithText(ownerName).performClick()

            assertTrue(hasClickedOnOwner)
        }
    }

    @Test
    fun clicking_on_the_photo_triggers_a_callback() {
        val ownerName = "John Smith"
        val ownerUrl = "ownerUrl"
        val photoUrl = "photoUrl"
        val title = "My cool image"

        var hasClickedOnImage = false

        composeTestRule.setContent {
            PhotoPostComponent(
                photoUrl = photoUrl,
                ownerUrl = ownerUrl,
                username = ownerName,
                title = title,
                onOwnerClick = {},
                tags = emptyList(),
                onImageClick = {
                    hasClickedOnImage = true
                },
                onTagClick = {}
            )
        }

        with(composeTestRule) {
            onNodeWithTag(PHOTO_COMPONENT_TEST_TAG).performClick()

            assertTrue(hasClickedOnImage)

            hasClickedOnImage = false

            onNodeWithText(title).performClick()

            assertTrue(hasClickedOnImage)
        }
    }
}