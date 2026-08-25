package com.weatherfeed.app.ui.forecast

import com.weatherfeed.app.utils.DailyForecast

sealed class ForecastUiState {
    object Loading : ForecastUiState()
    data object NoLocation : ForecastUiState()
    data class Success(val days: List<DailyForecast>) : ForecastUiState()
    data class Error(val throwable: Throwable) : ForecastUiState()
}
