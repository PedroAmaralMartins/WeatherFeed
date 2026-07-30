package com.weatherfeed.app.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weather.designsystem.WeatherConditionIcons
import com.weatherfeed.app.databinding.ItemForecastDayBinding

class ForecastAdapter : ListAdapter<MockForecastItem, ForecastAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(val binding: ItemForecastDayBinding) :
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
            conditionIcon = WeatherConditionIcons.fromOpenWeather(item.iconCode),
            conditionLabel = item.description,
            tempMax = "${item.tempMax.toInt()}°",
            tempMin = "${item.tempMin.toInt()}°"
        )
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MockForecastItem>() {
        override fun areItemsTheSame(
            oldItem: MockForecastItem,
            newItem: MockForecastItem
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: MockForecastItem,
            newItem: MockForecastItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}