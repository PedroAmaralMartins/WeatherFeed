package com.weatherfeed.app.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weatherfeed.app.databinding.ItemForecastDayBinding

class ForecastAdapter(
    private var item: List<MockForecastItem>
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(val biding: ItemForecastDayBinding) :
        RecyclerView.ViewHolder(biding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemForecastDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = item[position]
        holder.biding.weatherForecastRowView.bind(
            dayName = item.dayName,
            date = item.date,
            conditionIcon = item.iconCode,
            conditionLabel = item.description,
            tempMax = "${item.tempMax.toInt()}°",
            tempMin = "${item.tempMin.toInt()}°"
        )
    }

    override fun getItemCount() = item.size

    fun updateItems(newItem: List<MockForecastItem>) {
        item = newItem
        notifyDataSetChanged()
    }
}