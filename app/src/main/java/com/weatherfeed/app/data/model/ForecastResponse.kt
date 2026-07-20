package com.weatherfeed.app.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: ForecastMain,
    val weather: List<WeatherDesc>
)

data class ForecastMain(
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)

data class City(
    val name: String,
    val country: String
)
