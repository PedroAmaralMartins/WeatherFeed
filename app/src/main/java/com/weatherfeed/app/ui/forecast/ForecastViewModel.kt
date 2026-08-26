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
    }

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)

    val uiState = _uiState.asStateFlow()

    private var alreadyLoaded = false

    fun loadForecast(lat: Double, lon: Double, force: Boolean = false) {
        if (alreadyLoaded && !force) return
        alreadyLoaded = true

        viewModelScope.launch {
            _uiState.value = ForecastUiState.Loading
            repository.getForecast(lat, lon)
                .onSuccess { response ->
                    val days = ForecastUtils.groupByDay(response.list)
                    _uiState.value = ForecastUiState.Success(days)
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