package com.weatherfeed.app.utils

import kotlin.math.roundToInt

object TemperatureUtils {
    fun celsiusToFahrenheit(celsius: Double): Double =
        (celsius * 9.0 / 5.0) + 32.0

    fun formatTemp(celsius: Double, unit: String): String {
        val value = if (unit == PrefsManager.UNIT_CELSIUS) celsius
        else celsiusToFahrenheit(celsius)
        return "${value.roundToInt()}°"
    }

}