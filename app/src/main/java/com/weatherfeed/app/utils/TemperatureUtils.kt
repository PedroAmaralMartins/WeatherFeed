package com.weatherfeed.app.utils

import kotlin.math.roundToInt

object TemperatureUtils {
    private fun celsiusToFahrenheit(celsius: Double): Double =
        (celsius * 9.0 / 5.0) + 32.0

    fun formatTemp(celsius: Double, unit: String): String {
        val value = if (unit == PrefsManager.UNIT_CELSIUS) celsius
        else celsiusToFahrenheit(celsius)
        return "${value.roundToInt()}°"
    }

    fun formatTempWithUnit(celsius: Double, unit: String): String {
        val value = if (unit == PrefsManager.UNIT_CELSIUS) celsius
        else celsiusToFahrenheit(celsius)

        val label = if (unit == PrefsManager.UNIT_CELSIUS) "C" else "F"
        return "${value.roundToInt()}°$label"
    }

}