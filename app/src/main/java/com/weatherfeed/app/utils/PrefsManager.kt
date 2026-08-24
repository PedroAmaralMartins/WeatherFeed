package com.weatherfeed.app.utils

import android.content.Context

class PrefsManager(context: Context) {
    companion object     {
        const val UNIT_CELSIUS = "C"
        const val UNIT_FAHRENHEIT = "F"
        private const val KEY_TEMPERATURE_UNIT = "temperature_unit"
    }
    private val prefs = context.applicationContext
        .getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    var lastLatitude: Double
        get() = Double.fromBits(prefs.getLong("last_latitude", 0L))
        set(value) = prefs.edit().putLong("last_latitude", value.toBits()).apply()

    var lastLongitude: Double
        get() = Double.fromBits(prefs.getLong("last_longitude", 0L))
        set(value) = prefs.edit().putLong("last_longitude", value.toBits()).apply()

    fun hasLocation(): Boolean =
        prefs.contains("last_latitude") && prefs.contains("last_longitude")

    var temperatureUnit: String
        get() = prefs.getString(KEY_TEMPERATURE_UNIT, UNIT_CELSIUS) ?: UNIT_CELSIUS
        set(value) = prefs.edit().putString(KEY_TEMPERATURE_UNIT,value).apply()
}