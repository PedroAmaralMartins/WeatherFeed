package com.weatherfeed.app.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weatherfeed.app.data.model.GeocodingResponse
import com.weatherfeed.app.databinding.ItemWeatherCityRowBinding

class SearchAdapter(
    private val onCityClick: (GeocodingResponse) -> Unit
) : ListAdapter<GeocodingResponse, SearchAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: ItemWeatherCityRowBinding,
        private val onCityClick: (GeocodingResponse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(city: GeocodingResponse) {

            binding.cityRow.bind(
                cityName = city.name,
                country = city.state?.let { "$it, ${city.country}" } ?: city.country
            )

            binding.root.setOnClickListener {
                onCityClick(city)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherCityRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onCityClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<GeocodingResponse>() {
        override fun areItemsTheSame(
            oldItem: GeocodingResponse,
            newItem: GeocodingResponse
        ): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
        }

        override fun areContentsTheSame(
            oldItem: GeocodingResponse,
            newItem: GeocodingResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}