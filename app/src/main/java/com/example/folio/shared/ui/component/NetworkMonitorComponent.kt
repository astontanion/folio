package com.example.folio.shared.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.folio.R

const val NETWORK_MONITOR_COMPONENT_ICON_TEST_TAG = "CONNECTION_LOST_COMPONENT_ICON"

@Composable
fun NetworkMonitorComponent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(24.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.signal_disconnected_fill),
                contentDescription = null,
                modifier = Modifier.testTag(NETWORK_MONITOR_COMPONENT_ICON_TEST_TAG)
            )
            Text(text = stringResource(id = R.string.shared_network_monitor_error_message))
        }
    }
}

@Preview
@Composable
fun NetworkMonitorComponentPreview() {
    NetworkMonitorComponent(modifier = Modifier.fillMaxSize())
}