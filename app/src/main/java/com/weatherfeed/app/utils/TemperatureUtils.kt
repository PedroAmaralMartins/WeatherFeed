package com.weatherfeed.app.utils

import kotlin.math.roundToInt

object TemperatureUtils {
    private fun celsiusToFahrenheit(celsius: Double): Double =
        (celsius * 9.0 / 5.0) + 32.0

    fun formatTemp(celsius: Double, unit: String, showUnit: Boolean = false): String {
        val value = if (unit == PrefsManager.UNIT_CELSIUS) celsius
        else celsiusToFahrenheit(celsius)

        val unitSymbol = if (showUnit) {
            if (unit == PrefsManager.UNIT_CELSIUS) "C" else "F"
        } else ""

        return "${value.roundToInt()}°$unitSymbol"
    }

}