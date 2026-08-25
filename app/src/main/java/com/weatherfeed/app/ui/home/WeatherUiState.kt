package com.weatherfeed.app.ui.home

import com.weatherfeed.app.data.model.WeatherResponse

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val throwable: Throwable) : WeatherUiState()
}