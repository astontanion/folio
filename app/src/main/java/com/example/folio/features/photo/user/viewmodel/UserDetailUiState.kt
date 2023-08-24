package com.example.folio.features.photo.user.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.PhotosSummary

data class UserDetailUiState(
    val photoListSummaryResource: DataResource<PhotosSummary> = DataResource.Idle()
) {
    val isLoading = photoListSummaryResource.isWaiting()
}
