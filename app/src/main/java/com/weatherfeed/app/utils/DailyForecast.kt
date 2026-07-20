package com.weatherfeed.app.utils

import android.R.attr.data
import com.weatherfeed.app.data.model.ForecastItem

data class DailyForecast(
    val data: String,
    val dayName: String,
    val shortDate: String,
    val tempMax: Double,
    val tempMin: Double,
    val icon: String,
    val description: String
)

object ForecastUtils {
    fun groupByDay(item: List<ForecastItem>): List<DailyForecast> {
        return item.groupBy {
            it.dtTxt.substring(0, 10)
        }.map { (data, entries) ->
            val midEntry = entries[entries.size / 2]
            DailyForecast(
                data = date,
                dayName = formatDayName(date),
                shortDate = formatShortDate(date),
                tempMax = entries.maxOf { it.main.tempMax },
                tempMin = entries.minOf { it.main.tempMin },
                icon = midEntry.weather[0].icon,
                description = midEntry.weather[0].description
            )
        }
            .take(5)
    }
}




