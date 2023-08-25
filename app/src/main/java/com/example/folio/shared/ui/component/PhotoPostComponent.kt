package com.example.folio.shared.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPostComponent(
    modifier: Modifier = Modifier,
    photoUrl: String,
    ownerUrl: String,
    username: String,
    title: String,
    tags: List<String>,
    onOwnerClick: () -> Unit,
    onImageClick: () -> Unit,
    onTagClick: (tag: String) -> Unit,
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        UserInfoComponent(
            url = ownerUrl,
            username = username,
            location = null,
            modifier = Modifier.fillMaxWidth(),
                onClick = onOwnerClick
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PhotoComponent(
                url = photoUrl,
                contentDescription = null,
                onClick = onImageClick,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onImageClick() }
            )

            if (tags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    tags.forEach { tag ->
                        FilterChip(
                            selected = false,
                            onClick = { onTagClick(tag) },
                            label = { Text(text = tag) }
                        )
                    }
                }
            }
        }
    }
}