package com.weatherfeed.app.data.repository

import com.weatherfeed.app.data.remote.WeatherApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherRepository(
    private val apiKey: String
) {
   private val okHttp = OkHttpClient.Builder()
       .addInterceptor { chain ->
           val url = chain.request().url().newBuilder()
               .addQueryParameter("appid", apiKey)
               .build()
           chain.proceed(chain.request().newBuilder().url(url).build())
       }
       .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
