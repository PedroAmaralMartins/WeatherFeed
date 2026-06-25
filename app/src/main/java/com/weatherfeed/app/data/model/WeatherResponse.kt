package com.weatherfeed.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<WeatherDesc>,
    val wind: Wind,
    val coord: Coord
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelslike: Double,
    val humidity: Int
)

data class WeatherDesc(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)

data class Coord(
    val lat: Double,
    val lon: Double
)
