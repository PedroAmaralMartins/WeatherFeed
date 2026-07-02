package com.weatherfeed.app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){

    fun loadWeather(latitude: Double, longitude: Double) {
        Log.d("HomeViewModel", "Carregando clima para Lat: $latitude, Lon: $longitude")

    }
}