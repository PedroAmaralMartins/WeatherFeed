package com.weatherfeed.app.data.remote

import com.google.android.gms.common.api.internal.ApiKey
import com.weatherfeed.app.BuildConfig
import com.weatherfeed.app.data.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.openweathermap.org/"
    private val API_KEY = BuildConfig.WEATHER_API_KEY

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherResponse> {
        return try {
            Result.success(api.getCurrentWeather(lat, lon, API_KEY))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}