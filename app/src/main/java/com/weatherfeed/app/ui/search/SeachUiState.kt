package com.weatherfeed.app.ui.search

import com.weatherfeed.app.data.model.GeocodingResponse

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Empty : SearchUiState
    data class Success(val cities: List<GeocodingResponse>) : SearchUiState
    data class Error(val throwable: Throwable) : SearchUiState
}