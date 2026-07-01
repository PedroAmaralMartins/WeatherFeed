package com.weatherfeed.app.data.remote

import com.weatherfeed.app.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.openweathermap.org/"
    private val API_KEY = BuildConfig.WEATHER_API_KEY

    private val okHttp = OkHttpClient.Builder().addInterceptor { chain ->
        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", API_KEY)
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }
        .build()


    internal val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}