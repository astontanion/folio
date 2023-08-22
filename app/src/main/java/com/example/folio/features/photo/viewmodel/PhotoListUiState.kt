package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isSuccess
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode

data class PhotoListUiState(
    val searchQuery: String = "",
    val searchTags: List<String> = emptyList(),
    val searchMode: SearchTagMode = SearchTagMode.ANY,
    val encounteredTags: List<String> = emptyList(),
    val filteredTags: List<String> = emptyList(),
    val photoListSummaryResource: DataResource<PhotosSummary> = DataResource.Idle()
) {
    val isLoading: Boolean = photoListSummaryResource.isWaiting()
}
