package com.example.folio.shared.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PhotoPostComponent(
    modifier: Modifier = Modifier,
    photoUrl: String,
    ownerUrl: String,
    ownerName: String,
    title: String,
    onOwnerClick: () -> Unit,
    onImageClick: (url: String) -> Unit,
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        PostHeader(
            ownerUrl = ownerUrl,
            ownerName = ownerName,
            modifier = Modifier.fillMaxWidth(),
                onClick = onOwnerClick
        )

        Box {
            PhotoComponent(
                url = photoUrl,
                contentDescription = null,
                onClick = onImageClick
            )

            Box(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onImageClick(photoUrl)
                    }
                    .align(alignment = Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun PostHeader(
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