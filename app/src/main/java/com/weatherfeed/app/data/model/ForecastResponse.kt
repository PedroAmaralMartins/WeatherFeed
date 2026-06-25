package com.weatherfeed.app.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<WeatherDesc>
)

data class ForecastMain(
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("tem_min")
    val temMin: Double
)

data class City(
    val name: String,
    val country: String
)
