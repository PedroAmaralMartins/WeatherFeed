package com.weatherfeed.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.weatherfeed.app.data.model.GeocodingResponse
import com.weatherfeed.app.data.repository.WeatherRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_MS = 500L
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.value = SearchUiState.Idle
                        return@collectLatest
                    }
                    performSearch(query)
                }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.value = SearchUiState.Loading
        repository.searchCity(query)
            .onSuccess {
                _uiState.value = SearchUiState.Success(it)
            }
            .onFailure {
                _uiState.value = SearchUiState.Error(it)
            }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

}

sealed interface SearchUiState {

    data object Idle : SearchUiState

    data object Loading : SearchUiState

    data class Success(val cities: List<GeocodingResponse>) : SearchUiState

    data class Error(val message: Throwable) : SearchUiState
}

class SearchViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}