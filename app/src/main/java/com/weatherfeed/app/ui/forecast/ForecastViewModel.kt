package com.weatherfeed.app.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.weatherfeed.app.data.repository.WeatherRepository
import com.weatherfeed.app.utils.AppContainer
import com.weatherfeed.app.utils.ForecastUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ForecastViewModel(AppContainer.repository)
            }
        }
        private const val CACHE_DURATION_MS = 15 * 60 * 1000L
    }

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var lastLoadedAt: Long = 0

    fun loadForecast(lat: Double, lon: Double, force: Boolean = false) {
        val now = System.currentTimeMillis()
        val cacheStillValid = (now - lastLoadedAt) < CACHE_DURATION_MS

        if (!force && cacheStillValid && _uiState.value is ForecastUiState.Success) {
            return
        }
        viewModelScope.launch {
            _uiState.value = ForecastUiState.Loading
            repository.getForecast(lat, lon)
                .onSuccess { response ->
                    val days = ForecastUtils.groupByDay(response.list)
                    _uiState.value = ForecastUiState.Success(days)
                    lastLoadedAt = System.currentTimeMillis()
                }
                .onFailure {
                    _uiState.value =
                        ForecastUiState.Error(it)
                }
        }
    }

    fun onNoLocation() {
        _uiState.value = ForecastUiState.NoLocation
    }
}