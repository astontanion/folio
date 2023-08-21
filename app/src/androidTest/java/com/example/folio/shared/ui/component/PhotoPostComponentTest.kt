package com.example.folio.shared.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
                ownerName = ownerName,
                title = title,
                onOwnerClick = {},
                onImageClick = {}
            )
        }

        with(composeTestRule) {
            onNodeWithTag(AVATAR_IMAGE_TEST_TAG).assertIsDisplayed()
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
                ownerName = ownerName,
                title = title,
                onOwnerClick = {
                    hasClickedOnOwner = true
                },
                onImageClick = {}
            )
        }

        with(composeTestRule) {
            onNodeWithTag(AVATAR_IMAGE_TEST_TAG, useUnmergedTree = true).performClick()

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
                ownerName = ownerName,
                title = title,
                onOwnerClick = {},
                onImageClick = {
                    hasClickedOnImage = true
                }
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