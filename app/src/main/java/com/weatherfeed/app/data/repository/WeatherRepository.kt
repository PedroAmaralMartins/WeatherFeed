package com.weatherfeed.app.data.repository

import com.weatherfeed.app.data.model.ForecastResponse
import com.weatherfeed.app.data.model.WeatherResponse
import com.weatherfeed.app.data.remote.WeatherApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherRepository(
    private val api: WeatherApiService,

    private val apiKey: String
) {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherResponse> {
        return try {
            Result.success(api.getCurrentWeather(lat, lon, apiKey))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForecast(lat: Double, lon: Double): Result<ForecastResponse> {
        return try {
            Result.success(api.getForecast(lat, lon, apiKey))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSearchCity(cityName: String): Result<WeatherResponse> {

        return try {
            Result.success(api.searchCity(cityName, apiKey))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
