package com.weatherfeed.app.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weather.designsystem.WeatherConditionIcons
import com.weatherfeed.app.databinding.ItemForecastDayBinding
import com.weatherfeed.app.utils.DailyForecast

class ForecastAdapter : ListAdapter<DailyForecast, ForecastAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemForecastDayBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemForecastDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.weatherForecastRowView.bind(
            dayName = item.dayName,
            date = item.date,
            conditionIcon = WeatherConditionIcons.fromOpenWeather(item.icon),
            conditionLabel = item.description,
            tempMax = "${item.tempMax.toInt()}°",
            tempMin = "${item.tempMin.toInt()}°"
        )
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DailyForecast>() {
        override fun areItemsTheSame(
            oldItem: DailyForecast,
            newItem: DailyForecast
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: DailyForecast,
            newItem: DailyForecast
        ): Boolean {
            return oldItem == newItem
        }
    }
}