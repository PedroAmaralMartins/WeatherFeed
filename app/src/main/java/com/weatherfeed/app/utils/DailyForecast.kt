package com.weatherfeed.app.utils

import com.weatherfeed.app.data.model.ForecastItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class DailyForecast(
    val date: String,
    val dayName: String,
    val shortDate: String,
    val tempMax: Double,
    val tempMin: Double,
    val icon: String,
    val description: String
)

private const val DEFAULT_ICON = "01d"


object ForecastUtils {

    private val PT_BR = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun groupByDay(item: List<ForecastItem>): List<DailyForecast> {
        return item.groupBy {
            it.dtTxt.substring(0, 10)
        }.map { (date, entries) ->
            val midEntry = entries.minByOrNull { entry ->
                val hour = LocalDateTime.parse(entry.dtTxt, formatter).hour
                kotlin.math.abs(hour - 12)
            } ?: entries.first()
            DailyForecast(
                date = date,
                dayName = formatDayName(date),
                shortDate = formatShortDate(date),
                tempMax = entries.maxOf { it.main.tempMax },
                tempMin = entries.minOf { it.main.tempMin },
                icon = midEntry.weather.firstOrNull()?.icon ?: DEFAULT_ICON,
                description = midEntry.weather[0].description
            )
        }
            .take(5)
    }


    fun formatDayName(dateString: String): String {
        val date = LocalDate.parse(dateString)
        val today = LocalDate.now()

        return if (date == today) {
            "Hoje"
        } else {
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, PT_BR)
                .replace(".", "")
                .replaceFirstChar { it.uppercase() }
        }
    }

    fun formatShortDate(dateString: String): String {
        return LocalDate.parse(dateString)
            .format(DateTimeFormatter.ofPattern("dd MMM", PT_BR))
            .replace(".", "")
            .replaceFirstChar { it.uppercase() }
    }
}




