package com.example.folio.features.photo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.folio.core.di.Default
import com.example.folio.core.network.DataResource
import com.example.folio.features.photo.model.PhotosSummary
import com.example.folio.features.photo.model.SearchTagMode
import com.example.folio.features.photo.usecase.RetrievePhotosUseCase
import com.example.folio.features.photo.usecase.RetrieveUserPhotosByUsernameUseCase
import com.example.folio.features.photo.usecase.SearchPhotosUseCase
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
    private val searchPhotosUseCase: SearchPhotosUseCase,
    private val retrieveUserPhotosByUsernameUseCase: RetrieveUserPhotosByUsernameUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotoListUiState())
    val uiState: StateFlow<PhotoListUiState> = _uiState

    fun onRefresh() {
        retrievePhotos()
    }

    fun onQueryChange(query: String) {
        when (query.startsWith("@")) {
            true -> onUsernameChange(query)
            false -> onTagChange(query)
        }
    }

    private fun onTagChange(query: String) {
        val tags = when (query.isBlank()) {
            true -> emptyList()
            false -> query.split(",").mapNotNull {
                if (it.isBlank()) return@mapNotNull null
                it.trim()
            }
        }

        val lastTag = tags.lastOrNull().orEmpty()

        val filteredTags = when (query.isBlank())  {
            true -> uiState.value.encounteredTags
            false -> uiState.value.encounteredTags.filter { tag -> tag.contains(lastTag) }.distinct()
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                searchTags = tags,
                filteredTags = filteredTags
            )
        }
    }

    private fun onUsernameChange(query: String) {
        val username = query.removePrefix("@")

        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredUsernames = when (username.isBlank())  {
                    true -> uiState.value.encounteredUsernames
                    false -> it.encounteredUsernames.filter { name -> name.lowercase().contains(username.lowercase()) }.distinct()
                }
            )
        }
    }

    fun onAutoCompleteTag(tag: String) {
        if (uiState.value.searchQuery.startsWith("@")) {
            _uiState.update {
                it.copy(searchQuery = "@$tag")
            }
            return
        }

        val tags = uiState.value.searchTags

        val weights = tags.map {
            when (tag.contains(it)) {
                true -> it.length / tag.length.toFloat()
                false -> 0f
            }
        }

        weights.maxOfOrNull { it }?.let { weight ->
            val position = weights.indexOf(weight)

            val newTags = tags.mapIndexed { index, value ->
                when (index == position) {
                    true -> tag
                    false -> value
                }
            }

            val newQuery = newTags.joinToString(", ")

            _uiState.update {
                it.copy(
                    searchTags = newTags,
                    searchQuery = newQuery
                )
            }
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
        val query = uiState.value.searchQuery
        when (query.startsWith("@")) {
            true -> searchByUsername(query.removePrefix("@"))
            false -> searchByTag()
        }
    }

    private fun searchByTag() {
        viewModelScope.launch(dispatcher) {
            with(uiState.value) {
                searchPhotosUseCase(
                    tags = searchTags,
                    tagMode = searchMode
                ).collectLatest { resource ->
                    val tags = populateEncounteredTags(resource)
                    val usernames = populateEncounteredUsername(resource)

                    _uiState.update {
                        it.copy(
                            encounteredTags = tags,
                            encounteredUsernames = usernames,
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
                val usernames = populateEncounteredUsername(resource)

                _uiState.update {
                    it.copy(
                        encounteredTags = tags,
                        encounteredUsernames = usernames,
                        photoListSummaryResource = resource,
                    )
                }
            }
        }
    }

    private fun searchByUsername(username: String) {
        viewModelScope.launch(dispatcher) {
            retrieveUserPhotosByUsernameUseCase(username).collectLatest { resource ->
                val tags = populateEncounteredTags(resource)
                val usernames = populateEncounteredUsername(resource)

                _uiState.update {
                    it.copy(
                        encounteredTags = tags,
                        encounteredUsernames = usernames,
                        photoListSummaryResource = resource,
                    )
                }
            }
        }
    }

    private fun populateEncounteredUsername(resource: DataResource<PhotosSummary>): List<String> {
        val usernames =when (resource is DataResource.Success) {
            true -> {
                val newUsernames = resource.data.photos
                    .map { it.owner.username }

                val usernames = (uiState.value.encounteredUsernames + newUsernames)
                    .distinct()
                    .shuffled()


                // making sure that we only store at most 1000 random tags.
                when (usernames.isEmpty()) {
                    true -> usernames
                    false -> usernames.subList(0, usernames.lastIndex.coerceAtMost(1_000) + 1)
                }.sorted()
            }
            false -> uiState.value.encounteredUsernames
        }

        return usernames
    }

    private fun populateEncounteredTags(resource: DataResource<PhotosSummary>): List<String> {
        return when (resource is DataResource.Success) {
            true -> {
                val newTags = resource.data.photos
                    .flatMap { it.tags }

                val tags = (uiState.value.encounteredTags + newTags)
                    .distinct()
                    .shuffled()

                // making sure that we only store at most 1000 random tags.
                when (tags.isEmpty()) {
                    true -> tags
                    false -> tags.subList(0, tags.lastIndex.coerceAtMost(1_000) + 1)
                }.sorted()
            }
            false -> uiState.value.encounteredTags
        }
    }
}