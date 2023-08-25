package com.example.folio.shared.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

const val PHOTO_COMPONENT_TEST_TAG = "PHOTO_COMPONENT"

@Composable
fun PhotoComponent(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String?,
    shape: Shape? = RoundedCornerShape(8.dp),
    onClick: () -> Unit
) {
    val clipModifier = when (shape == null) {
        true -> modifier
        false -> Modifier.clip(shape)
    }
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .aspectRatio(ratio = 16 / 9f)
            .then(clipModifier)
            .testTag(PHOTO_COMPONENT_TEST_TAG)
    )
}