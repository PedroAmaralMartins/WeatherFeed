package com.weatherfeed.app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weatherfeed.app.data.remote.WeatherApiService

class HomeViewModel : ViewModel() {
    private val _weatherState = MutableLiveData<WeatherUiState>()
    val weatherState: LiveData<WeatherUiState> = _weatherState
    fun loadWeather(latitude: Double, longitude: Double) {
    }
}
sealed interface WeatherUiState {
    object Loeading : WeatherUiState
    data class Success(val weatherData: String) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}