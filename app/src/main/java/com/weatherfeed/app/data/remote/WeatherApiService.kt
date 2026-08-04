package com.weatherfeed.app.data.remote

import com.weatherfeed.app.data.model.ForecastResponse
import com.weatherfeed.app.data.model.GeocodingResponse
import com.weatherfeed.app.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ):
            WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 40
    ):
            ForecastResponse

    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") city: String,
        @Query("limit") limit: Int = 10
    ): List<GeocodingResponse>
}