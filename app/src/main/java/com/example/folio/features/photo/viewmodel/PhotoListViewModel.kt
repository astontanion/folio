package com.example.folio.features.photo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.folio.core.di.Default
import com.example.folio.features.photo.usecase.RetrievePhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    @Default
    private val dispatcher: CoroutineDispatcher,
    private val retrievePhotosUseCase: RetrievePhotosUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotoListUiState())
    val uiState: StateFlow<PhotoListUiState> = _uiState

    fun onRefresh() {
        retrievePhotos()
    }

    private fun retrievePhotos() {
        viewModelScope.launch(dispatcher) {
            retrievePhotosUseCase().collectLatest { resource ->
                _uiState.update {
                    it.copy(photoListSummaryResource = resource)
                }
            }
        }
    }
}