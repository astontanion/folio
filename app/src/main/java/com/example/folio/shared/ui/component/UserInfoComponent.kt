package com.example.folio.shared.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserInfoComponent(
    modifier: Modifier = Modifier,
    ownerUrl: String,
    ownerName: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AvatarImageComponent(
            url = ownerUrl,
            contentDescription = null,
            onClick = { onClick() }
        )
        Text(
            text = ownerName,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}