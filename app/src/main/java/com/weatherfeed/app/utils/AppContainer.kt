package com.weatherfeed.app.utils

import com.weatherfeed.app.data.remote.RetrofitClient
import com.weatherfeed.app.data.repository.WeatherRepository

object AppContainer {
    val repository by lazy {
        WeatherRepository(RetrofitClient.api)
    }
}