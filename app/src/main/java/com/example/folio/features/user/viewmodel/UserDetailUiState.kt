package com.example.folio.features.user.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.PhotosSummary

data class UserDetailUiState(
    val photoListSummaryResource: DataResource<PhotosSummary> = DataResource.Idle()
) {
    companion object {}

    val isLoading = photoListSummaryResource.isWaiting()
}
