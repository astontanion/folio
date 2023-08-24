package com.example.folio.shared.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

const val AVATAR_IMAGE_TEST_TAG = "AVATAR_IMAGE"

@Composable
fun AvatarImageComponent(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String?,
    onClick: (url: String) -> Unit
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(40.dp)
            .clickable { onClick(url) }
            .clip(shape = CircleShape)
            .testTag(AVATAR_IMAGE_TEST_TAG)
    )
}