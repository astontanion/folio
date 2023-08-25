package com.example.folio.features.photo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.folio.R
import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isSuccess
import com.example.folio.features.photo.model.SearchTagMode
import com.example.folio.features.photo.model.SearchTagMode.ALL
import com.example.folio.features.photo.model.SearchTagMode.ANY
import com.example.folio.features.photo.viewmodel.PhotoListUiState
import com.example.folio.features.photo.viewmodel.PhotoListViewModel
import com.example.folio.shared.ui.component.MessageComponent
import com.example.folio.shared.ui.component.PhotoPostComponent

const val SEARCH_BAR_TEST_TAG = "SEARCH_BAR_TEST_TAG"
const val SEARCH_BAR_SUGGESTIONS = "SEARCH_BAR_SUGGESTIONS"

@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotoListViewModel = hiltViewModel(),
    onUserClick: (userId: String) -> Unit,
    onPhotoClick: (photoId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (!uiState.photoListSummaryResource.isSuccess()) {
            viewModel.onRefresh()
        }
    }

    PhotoListScreen(
        modifier = modifier,
        uiState = uiState,
        onRefresh = viewModel::onRefresh,
        onQueryChange = viewModel::onQueryChange,
        onSearch = {
            when (it.isBlank()) {
                true -> viewModel.onRefresh()
                false -> viewModel.onSearch()
            }
        },
        onSearchModeChange = {
            viewModel.onSearchModeChange(it)
            viewModel.onSearch()
        },
        onAutoComplete = viewModel::onAutoCompleteTag,
        onUserClick = onUserClick,
        onPhotoClick = onPhotoClick,
        onTagClick = { tag ->
            viewModel.onQueryChange(tag)
            viewModel.onSearch()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    uiState: PhotoListUiState,
    onRefresh: () -> Unit,
    onQueryChange: (query: String) -> Unit,
    onSearch: (query: String) -> Unit,
    onSearchModeChange: (mode: SearchTagMode) -> Unit,
    onAutoComplete: (tag: String) -> Unit,
    onUserClick: (userId: String) -> Unit,
    onPhotoClick: (photoId: String) -> Unit,
    onTagClick: (tag: String) -> Unit
) {

    val pullState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onRefresh
    )

    val listState = rememberLazyListState()

    var isSearchActive by rememberSaveable {
        mutableStateOf(false)
    }

    Box (
        modifier = modifier.pullRefresh(pullState)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = onQueryChange,
                onSearch = {
                    isSearchActive = false
                    onSearch(it)
                },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                placeholder = { Text(text = stringResource(id = R.string.search_for_photo_by_tag)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (!uiState.searchQuery.startsWith("@")) {
                        Row {
                            var showDropDown by remember { mutableStateOf(false) }

                            Text(
                                text = stringResource(
                                    id = when (uiState.searchMode) {
                                        ANY -> R.string.search_mode_any
                                        ALL -> R.string.search_mode_all
                                    }
                                )
                            )

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    showDropDown = true
                                }
                            )

                            DropdownMenu(
                                expanded = showDropDown,
                                onDismissRequest = { showDropDown = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        showDropDown = false
                                        onSearchModeChange(ANY)
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.search_mode_any))
                                }

                                DropdownMenuItem(
                                    onClick = {
                                        showDropDown = false
                                        onSearchModeChange(ALL)
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.search_mode_all))
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.testTag(SEARCH_BAR_TEST_TAG)
            ) {
                LazyColumn(
                    modifier = Modifier.testTag(SEARCH_BAR_SUGGESTIONS)
                ) {
                    when (uiState.searchQuery.startsWith("@")) {
                        true -> {
                            items(items = uiState.filteredUsernames, key = { it }) { username ->
                                ListItem(
                                    headlineContent = { Text(text = username) },
                                    supportingContent = null,
                                    leadingContent = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.alternate_email_fill),
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.primary
                                        )
                                    },
                                    modifier = Modifier
                                        .clickable {
                                            onAutoComplete(username)
                                        }
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        false -> {
                            items(items = uiState.filteredTags, key = { it }) {tag ->
                                ListItem(
                                    headlineContent = { Text(text = tag) },
                                    supportingContent = null,
                                    leadingContent = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.tag_fill),
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.primary
                                        )
                                    },
                                    modifier = Modifier
                                        .clickable {
                                            onAutoComplete(tag)
                                        }
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (uiState.photoListSummaryResource) {
                    is DataResource.Idle,
                    is DataResource.Waiting -> {}
                    is DataResource.Success -> {
                        val items = uiState.photoListSummaryResource.data
                        when (items.photos.isEmpty()) {
                            true -> {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize()
                                    ) {
                                        MessageComponent(
                                            icon = R.drawable.info_fill,
                                            message = stringResource(id = R.string.photos_not_found),
                                            action = stringResource(id = R.string.button_get_recent),
                                            onActionClick = onRefresh,
                                        )
                                    }
                                }
                            }
                            false -> {
                                items(items = items.photos, key = { it.id }) {photo ->
                                    PhotoPostComponent(
                                        photoUrl = photo.url,
                                        ownerUrl = photo.owner.profileUrl,
                                        username = photo.owner.username,
                                        title = photo.title,
                                        tags = photo.tags,
                                        onImageClick = {
                                            onPhotoClick(photo.id)
                                        },
                                        onOwnerClick = {
                                            onUserClick(photo.owner.id)
                                        },
                                        onTagClick = onTagClick
                                    )
                                }
                            }
                        }
                    }
                    is DataResource.Failure -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(16.dp)
                            ) {
                                MessageComponent(
                                    icon = R.drawable.error_fill,
                                    message = stringResource(id = R.string.unable_to_fetch_photos),
                                    action = stringResource(id = R.string.button_try_again),
                                    onActionClick = onRefresh,
                                )
                            }
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