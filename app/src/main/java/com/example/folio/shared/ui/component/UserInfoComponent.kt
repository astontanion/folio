package com.example.folio.shared.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val USER_INFO_IMAGE_TEST_TAG = "USER_INFO_IMAGE"
const val USER_INFO_LOCATION_TEST_TAG = "USER_INFO_LOCATION"

@Composable
fun UserInfoComponent(
    modifier: Modifier = Modifier,
    url: String,
    username: String,
    location: String?,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AvatarImageComponent(
            url = url,
            contentDescription = null,
            onClick = { onClick() },
            modifier = Modifier.testTag(USER_INFO_IMAGE_TEST_TAG)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.clickable {
                    onClick()
                }
            )

            location?.let {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .testTag(USER_INFO_LOCATION_TEST_TAG)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }
        }

    }
}