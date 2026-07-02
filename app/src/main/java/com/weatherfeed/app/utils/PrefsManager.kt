package com.weatherfeed.app.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    var lastLatitude: Double
        get() = prefs.getFloat("last_latitude", 0.0f).toDouble()
        set(value) = prefs.edit().putFloat("last_latitude", value.toFloat()).apply()

    var lastLongitude: Double
        get() = prefs.getFloat("last_longitude", 0.0f).toDouble()
        set(value) = prefs.edit().putFloat("last_longitude", value.toFloat()).apply()
}