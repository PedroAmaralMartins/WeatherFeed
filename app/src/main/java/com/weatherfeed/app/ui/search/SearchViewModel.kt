package com.weatherfeed.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.weatherfeed.app.data.model.GeocodingResponse
import com.weatherfeed.app.data.repository.WeatherRepository
import com.weatherfeed.app.utils.AppContainer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(AppContainer.repository)
            }
        }

        private const val SEARCH_DEBOUNCE_MS = 500L
    }

    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.value = SearchUiState.Idle
                        return@collectLatest
                    }
                    _uiState.value = SearchUiState.Loading
                    repository.searchCity(query)
                        .onSuccess { cities ->
                            _uiState.value = if (cities.isEmpty()) {
                                SearchUiState.Empty
                            } else {
                                SearchUiState.Success(cities)
                            }
                        }
                        .onFailure { _uiState.value = SearchUiState.Error(it) }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

}

sealed interface SearchUiState {

    data object Idle : SearchUiState

    data object Loading : SearchUiState

    data object Empty: SearchUiState
    data class Success(val cities: List<GeocodingResponse>) : SearchUiState

    data class Error(val throwable: Throwable) : SearchUiState
}