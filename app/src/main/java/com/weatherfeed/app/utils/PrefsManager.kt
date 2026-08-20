package com.weatherfeed.app.utils

import android.content.Context
import androidx.core.content.edit

class PrefsManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    var lastLatitude: Double
        get() = Double.fromBits(prefs.getLong("last_latitude", 0L))
        set(value) = prefs.edit { putLong("last_latitude", value.toBits()) }

    var lastLongitude: Double
        get() = Double.fromBits(prefs.getLong("last_longitude", 0L))
        set(value) = prefs.edit { putLong("last_longitude", value.toBits()) }

    fun hasLocation(): Boolean =
        prefs.contains("last_latitude") && prefs.contains("last_longitude")

}