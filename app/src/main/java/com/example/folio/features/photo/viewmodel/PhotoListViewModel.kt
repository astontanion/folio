package com.example.folio.features.photo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.folio.core.di.Default
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import com.example.folio.features.photo.usecase.RetrievePhotosUseCase
import com.example.folio.features.photo.usecase.SearchPhotosUseCase
import com.example.folio.features.photo.usecase.SearchPhotosUseCaseImpl
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
    private val retrievePhotosUseCase: RetrievePhotosUseCase,
    private val searchPhotosUseCase: SearchPhotosUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotoListUiState())
    val uiState: StateFlow<PhotoListUiState> = _uiState

    fun onRefresh() {
        retrievePhotos()
    }

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredTags = when (query.isBlank())  {
                    true -> uiState.value.encounteredTags
                    false -> it.encounteredTags.filter { tag -> tag.contains(query) }.distinct()
                }
            )
        }
    }

    fun onTagsChange(tags: List<String>) {
        _uiState.update {
            it.copy(
                searchTags = tags
            )
        }
    }

    fun onSearchModeChange(searchMode: SearchTagMode) {
        _uiState.update {
            it.copy(
                searchMode = searchMode
            )
        }
    }

    fun onSearch() {
        viewModelScope.launch(dispatcher) {
            with(uiState.value) {
                searchPhotosUseCase(
                    tags = searchTags,
                    query = searchQuery,
                    tagMode = searchMode
                ).collectLatest { resource ->
                    val tags = populateEncounteredTags(resource)

                    _uiState.update {
                        it.copy(
                            encounteredTags = tags,
                            photoListSummaryResource = resource
                        )
                    }
                }
            }
        }
    }

    private fun retrievePhotos() {
        viewModelScope.launch(dispatcher) {
            retrievePhotosUseCase().collectLatest { resource ->
                val tags = populateEncounteredTags(resource)

                _uiState.update {
                    it.copy(encounteredTags = tags, photoListSummaryResource = resource)
                }
            }
        }
    }

    private fun populateEncounteredTags(resource: DataResource<PhotosSummary>): List<String> {
        return when (resource is DataResource.Success) {
            true -> {
                val list = resource.data.photos
                    .flatMap { it.tags }
                    .shuffled()
                    .distinct()
                    .sorted()

                // making sure that we only store at most 1000 random tags.
                when (list.isEmpty()) {
                    true -> list
                    false -> list.subList(0, list.lastIndex.coerceAtMost(1_000) + 1)
                }
            }
            false -> uiState.value.encounteredTags
        }
    }
}