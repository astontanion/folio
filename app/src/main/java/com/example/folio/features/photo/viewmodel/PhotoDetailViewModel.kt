package com.example.folio.features.photo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.folio.core.di.Default
import com.example.folio.features.photo.usecase.RetrievePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    @Default
    private val dispatcher: CoroutineDispatcher,
    private val retrievePhotoUseCase: RetrievePhotoUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotoDetailUiState())
    val uiState: StateFlow<PhotoDetailUiState> = _uiState

    fun onRefresh(photoId: String) {
        retrievePhoto(photoId)
    }

    private fun retrievePhoto(photoId: String) {
        viewModelScope.launch(dispatcher) {
            retrievePhotoUseCase(photoId).collectLatest { resource ->
                _uiState.update {
                    it.copy(photoResource = resource)
                }
            }
        }
    }
}