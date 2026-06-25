package com.weatherfeed.app.data.model

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<String>,
    val wind: Wind,
    val coord: Coord
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int
)

data class WeatherDesc(
    val Description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)

data class Coord(
    val lat: Double,
    val lon: Double
)
