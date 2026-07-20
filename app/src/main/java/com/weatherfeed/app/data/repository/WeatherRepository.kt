package com.weatherfeed.app.data.repository

import com.weatherfeed.app.data.model.ForecastResponse
import com.weatherfeed.app.data.model.WeatherResponse
import com.weatherfeed.app.data.remote.WeatherApiService
import kotlinx.coroutines.CancellationException


class WeatherRepository(
    private val api: WeatherApiService
) {

    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherResponse> {
        return try {
            Result.success(api.getCurrentWeather(lat, lon))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForecast(lat: Double, lon: Double, appid: String): Result<ForecastResponse> {
        return try {
            Result.success(api.getForecast(lat, lon, appid))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchCity(cityName: String): Result<WeatherResponse> {
        return try {
            Result.success(api.searchCity(cityName))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
