package com.weatherfeed.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.weatherfeed.app.data.model.WeatherResponse
import com.weatherfeed.app.data.repository.WeatherRepository
import com.weatherfeed.app.utils.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(AppContainer.repository)
            }
        }
    }
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            repository.getCurrentWeather(lat, lon)
                .onSuccess { _uiState.value = WeatherUiState.Success(it) }
                .onFailure { _uiState.value = WeatherUiState.Error(it) }
        }
    }
}


sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val throwable: Throwable) : WeatherUiState()
}
