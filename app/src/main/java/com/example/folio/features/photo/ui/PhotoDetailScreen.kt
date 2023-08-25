package com.example.folio.features.photo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.folio.R
import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isSuccess
import com.example.folio.features.photo.viewmodel.PhotoDetailUiState
import com.example.folio.features.photo.viewmodel.PhotoDetailViewModel
import com.example.folio.shared.ui.component.MessageComponent
import com.example.folio.shared.ui.component.PhotoComponent
import com.example.folio.shared.ui.component.UserInfoComponent

@Composable
fun PhotoDetailScreen(
    modifier: Modifier = Modifier,
    photoId: String,
    viewModel: PhotoDetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        if (!uiState.photoResource.isSuccess()) {
            viewModel.onRefresh(photoId)
        }
    }

    PhotoDetailScreen(
        modifier = modifier,
        uiState = uiState,
        onRefresh = { viewModel.onRefresh(photoId) },
        onBackPress = onBackPress
    )

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun PhotoDetailScreen(
    modifier: Modifier = Modifier,
    uiState: PhotoDetailUiState,
    onRefresh: () -> Unit,
    onBackPress: () -> Unit
) {
    val pullState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onRefresh
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.photo_detail_title),
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_go_back),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable {
                            onBackPress()
                        }
                    )
                }
            )
        },
        modifier = modifier
    ) { padding ->
        Box (
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullState)
        ) {
            when (uiState.photoResource) {
                is DataResource.Idle,
                is DataResource.Waiting -> {}
                is DataResource.Success -> {
                    val photo = uiState.photoResource.data

                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        PhotoComponent(
                            url = photo.url,
                            contentDescription = photo.title,
                            shape = null,
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                UserInfoComponent(
                                    url = photo.owner.profileUrl,
                                    username = photo.owner.username,
                                    location = photo.owner.location,
                                    onClick = {
                                        // do nothing
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                photo.dateTaken?.let {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DateRange,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            val dateComponent = photo.dateTaken.split(" ")
                                            Text(
                                                text = dateComponent.first(),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                            Text(
                                                text = dateComponent.last(),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }
                            

                            Text(
                                text = photo.title,
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            )

                            photo.description?.let {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.shared_description),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = photo.description)
                                }
                            }

                            if (photo.tags.isNotEmpty()) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.shared_tags),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(0.dp)
                                    ) {
                                        photo.tags.forEach { tag ->
                                            FilterChip(
                                                selected = false,
                                                onClick = {},
                                                label = { Text(text = tag) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
                is DataResource.Failure -> {
                    uiState.photoResource.error.printStackTrace()
                    MessageComponent(
                        icon = R.drawable.error_fill,
                        message = stringResource(id = R.string.unable_to_fetch_photo_detail),
                        action = stringResource(id = R.string.button_try_again),
                        onActionClick = onRefresh,
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

