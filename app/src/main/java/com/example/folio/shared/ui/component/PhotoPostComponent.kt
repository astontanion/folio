package com.example.folio.shared.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        UserInfoComponent(
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