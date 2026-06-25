package com.weatherfeed.data.model

data class ForecastResponse(
    val list: List<String>,
    val city: City
)

data class ForecastItem(
    val dt_txt: String,
    val main: ForecastMain,
    val weather: List<String>
)

data class ForecastMain(
    val temp_max: Double,
    val tem_min: Double
)

data class City(
    val nome: String,
    val country: String
)
