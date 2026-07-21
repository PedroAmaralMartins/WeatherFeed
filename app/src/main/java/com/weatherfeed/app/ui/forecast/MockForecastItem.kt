package com.weatherfeed.app.ui.forecast

data class MockForecastItem(
    val dayName: String,
    val date: String,
    val iconCode: String,
    val description: String,
    val tempMax: Double,
    val tempMin: Double
) {
    companion object {
        fun getMockList(): List<MockForecastItem> {
            return listOf(
                MockForecastItem(
                    dayName = "Hoje",
                    date = "21/07",
                    iconCode = "01d",
                    description = "Ensolarado",
                    tempMax = 28.0,
                    tempMin = 18.0
                )
            )
        }
    }
}