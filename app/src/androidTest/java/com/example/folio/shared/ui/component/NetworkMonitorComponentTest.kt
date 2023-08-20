package com.example.folio.shared.ui.component

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import com.example.folio.R

class NetworkMonitorComponentTest {

    @get:Rule
    val composeTestRules = createComposeRule()

    @Test
    fun hasIcon() {
        composeTestRules.setContent {
            NetworkMonitorComponent()
        }

        composeTestRules.onNodeWithTag(NETWORK_MONITOR_COMPONENT_ICON_TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun hasConnectionErrorMessage() {
        var message = ""

        composeTestRules.setContent {
            message = stringResource(id = R.string.shared_network_monitor_error_message)
            NetworkMonitorComponent()
        }

        composeTestRules.onNodeWithText(message)
            .assertIsDisplayed()
    }
}