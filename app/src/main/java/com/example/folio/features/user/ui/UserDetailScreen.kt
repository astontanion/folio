package com.example.folio.features.user.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.folio.R
import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isSuccess
import com.example.folio.features.user.viewmodel.UserDetailUiState
import com.example.folio.features.user.viewmodel.UserDetailViewModel
import com.example.folio.shared.ui.component.MessageComponent
import com.example.folio.shared.ui.component.PhotoComponent
import com.example.folio.shared.ui.component.UserInfoComponent

const val USER_INFO_COMPONENT_TEST_TAG = "USER_INFO_COMPONENT"

@Composable
fun UserDetailScreen(
    modifier: Modifier = Modifier,
    userId: String,
    viewModel: UserDetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        if (!uiState.photoListSummaryResource.isSuccess()) {
            viewModel.onRefresh(userId)
        }
    }

    UserDetailScreen(
        modifier = modifier,
        uiState = uiState,
        onRefresh = { viewModel.onRefresh(userId) },
        onBackPress = onBackPress
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    modifier: Modifier = Modifier,
    uiState: UserDetailUiState,
    onRefresh: () -> Unit,
    onBackPress: () -> Unit
) {

    val pullState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onRefresh
    )

    val gridState = rememberLazyGridState()

    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (uiState.photoListSummaryResource is DataResource.Success) {
                        uiState.photoListSummaryResource.data.photos.firstOrNull()?.let { photo ->
                            UserInfoComponent(
                                ownerUrl = photo.ownerProfileUrl,
                                ownerName = photo.ownerName,
                                onClick = {},
                                modifier = Modifier.testTag(USER_INFO_COMPONENT_TEST_TAG)
                            )
                        }
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_go_back),
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.clickable {
                            onBackPress()
                        }
                    )
                },
            )

        }
    ) { padding ->
        Box(
            modifier = modifier
                .padding(padding)
                .pullRefresh(pullState)
        ) {
            LazyVerticalGrid(
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                columns = GridCells.Adaptive(150.dp),
                modifier = modifier.padding(4.dp)
            ) {
                when (uiState.photoListSummaryResource) {
                    is DataResource.Idle,
                    is DataResource.Waiting -> {}
                    is DataResource.Success -> {
                        val photos = uiState.photoListSummaryResource.data.photos
                        when (photos.isEmpty()) {
                            true -> {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        MessageComponent(
                                            icon = R.drawable.info_fill,
                                            message = stringResource(id = R.string.user_photos_not_found),
                                            action = stringResource(id = R.string.button_try_again),
                                            onActionClick = onRefresh,
                                        )
                                    }
                                }
                            }
                            false -> {
                                items(items = photos, key = { it.id }) { photo ->
                                    PhotoComponent(
                                        url = photo.url,
                                        contentDescription = photo.title,
                                        onClick = {
                                            // TODO: navigate to photo detail
                                        }
                                    )
                                }
                            }
                        }
                    }
                    is DataResource.Failure -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                MessageComponent(
                                    icon = R.drawable.error_fill,
                                    message = stringResource(id = R.string.unable_to_fetch_user_photos),
                                    action = stringResource(id = R.string.button_try_again),
                                    onActionClick = onRefresh,
                                )
                            }
                        }
                    }
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