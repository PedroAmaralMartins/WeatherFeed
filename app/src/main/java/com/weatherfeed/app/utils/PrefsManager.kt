package com.weatherfeed.app.utils

import android.content.Context

class PrefsManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    var lastLatitude: Double
        get() = Double.fromBits(prefs.getLong("last_latitude", 0L))
        set(value) = prefs.edit().putLong("last_latitude", value.toBits()).apply()

    var lastLongitude: Double
        get() = prefs.getFloat("last_longitude", 0.0f).toDouble()
        set(value) = prefs.edit().putFloat("last_longitude", value.toFloat()).apply()
}