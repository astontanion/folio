package com.example.folio.features.photo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.folio.core.di.Default
import com.example.folio.features.photo.usecase.RetrieveRecentPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    @Default
    private val dispatcher: CoroutineDispatcher,
    private val retrieveRecentPhotoUseCase: RetrieveRecentPhotoUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotoListUiState())
    val uiState: StateFlow<PhotoListUiState> = _uiState

    fun onRefresh() {
        retrievePhotos()
    }

    private fun retrievePhotos() {
        viewModelScope.launch(dispatcher) {
            retrieveRecentPhotoUseCase().collectLatest { resource ->
                _uiState.update {
                    it.copy(photoListSummaryResource = resource)
                }
            }
        }
    }
}