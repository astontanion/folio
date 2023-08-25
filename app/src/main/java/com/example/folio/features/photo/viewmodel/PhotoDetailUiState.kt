package com.example.folio.features.photo.viewmodel

import com.example.folio.core.network.DataResource
import com.example.folio.core.network.isWaiting
import com.example.folio.features.photo.model.Photo

data class PhotoDetailUiState(
    val photoResource: DataResource<Photo> = DataResource.Idle()
) {
    val isLoading = photoResource.isWaiting()
}
