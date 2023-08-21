package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.PhotosSummary

data class PhotoListUiState(
    val photoListSummaryResource: DataResource<PhotosSummary> = DataResource.Idle()
) {
    val isLoading: Boolean = photoListSummaryResource.isWaiting()
}
