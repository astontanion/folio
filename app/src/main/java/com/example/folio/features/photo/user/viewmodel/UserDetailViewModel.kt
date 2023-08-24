package com.example.folio.features.photo.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.folio.core.di.Default
import com.example.folio.features.photo.usecase.RetrieveUserPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    @Default
    private val dispatcher: CoroutineDispatcher,
    private val retrieveUserPhotosUseCase: RetrieveUserPhotosUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailUiState())
    val uiState: StateFlow<UserDetailUiState> = _uiState

    fun onRefresh(userId: String) {
        retrieveUserPhotos(userId)
    }

    private fun retrieveUserPhotos(userId: String) {
        viewModelScope.launch(dispatcher) {
            retrieveUserPhotosUseCase(userId = userId).collectLatest { resource ->
                _uiState.update {
                    it.copy(
                        photoListSummaryResource = resource
                    )
                }
            }
        }
    }
}