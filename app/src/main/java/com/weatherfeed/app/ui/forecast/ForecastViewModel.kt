package com.weatherfeed.app.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.weatherfeed.app.data.repository.WeatherRepository
import com.weatherfeed.app.utils.DailyForecast
import com.weatherfeed.app.utils.ForecastUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)

     val uiState = _uiState.asStateFlow()

    fun loadForecast(lat: Double, lon: Double) {
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
}

sealed class ForecastUiState {
    object Loading : ForecastUiState()

    data class Success(val days: List<DailyForecast>) : ForecastUiState()

    data class Error(val message: Throwable) : ForecastUiState()
}

class ForecastViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForecastViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}