package com.example.folio.features.user.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary

fun UserDetailUiState.Companion.create(
    photoListSummaryResource: DataResource<PhotosSummary> = DataResource.Idle()
): UserDetailUiState {
    return UserDetailUiState(photoListSummaryResource = photoListSummaryResource)
}