package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode

fun PhotoListUiState.Companion.create(
    searchQuery: String = "",
    searchTags: List<String> = emptyList(),
    searchMode: SearchTagMode = SearchTagMode.ANY,
    encounteredTags: List<String> = emptyList(),
    filteredTags: List<String> = emptyList(),
    photoListSummaryResource: DataResource<PhotosSummary> = DataResource.Idle()
): PhotoListUiState {
    return PhotoListUiState(
        searchQuery = searchQuery,
        searchTags = searchTags,
        searchMode = searchMode,
        encounteredTags = encounteredTags,
        filteredTags = filteredTags,
        photoListSummaryResource = photoListSummaryResource
    )
}