package com.weatherfeed.app.ui.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherfeed.app.R
import com.weatherfeed.app.databinding.FragmentForecastBinding
import com.weatherfeed.app.utils.PrefsManager
import kotlinx.coroutines.launch
import java.io.IOException

class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    private val viewModel: ForecastViewModel by viewModels {
        ForecastViewModel.Factory
    }
    private lateinit var prefsManager: PrefsManager
    private lateinit var adapter: ForecastAdapter

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentForecastBinding.bind(view)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        prefsManager = PrefsManager(requireContext())
        adapter = ForecastAdapter(prefsManager.temperatureUnit)
        binding.recyclerview.adapter = adapter

        binding.btnRetry.setOnClickListener {
            viewModel.loadForecast(
                prefsManager.lastLatitude,
                prefsManager.lastLongitude,
                true
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ForecastUiState.NoLocation -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorContainer.visibility = View.VISIBLE
                            binding.tvErrorMessage.visibility = View.VISIBLE
                            binding.tvErrorMessage.text = getString(R.string.no_location)

                            binding.btnRetry.visibility = View.GONE
                        }

                        is ForecastUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvErrorMessage.visibility = View.GONE
                            binding.tvnextFewDays.visibility = View.GONE
                            binding.tvfiveDayForecast.visibility = View.GONE
                            binding.recyclerview.visibility = View.GONE
                            binding.btnRetry.visibility = View.GONE
                            binding.errorContainer.visibility = View.GONE

                        }

                        is ForecastUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvnextFewDays.visibility = View.VISIBLE
                            binding.tvfiveDayForecast.visibility = View.VISIBLE
                            binding.recyclerview.visibility = View.VISIBLE
                            binding.btnRetry.visibility = View.GONE
                            binding.errorContainer.visibility = View.GONE
                            adapter.submitList(state.days)
                        }

                        is ForecastUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorContainer.visibility = View.VISIBLE
                            binding.tvErrorMessage.visibility = View.VISIBLE
                            binding.tvErrorMessage.text = mapErrorMessage(state.throwable)
                            binding.btnRetry.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
    private fun mapErrorMessage(throwable: Throwable): String {
        return if (throwable is IOException) {
            getString(R.string.error_network)
        } else {
            getString(R.string.error_loading_forecast)
        }
    }

    private fun fetchForecast() {
        if (prefsManager.hasLocation()) {
            viewModel.loadForecast(prefsManager.lastLatitude, prefsManager.lastLongitude)
        } else {
            viewModel.onNoLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUnit = prefsManager.temperatureUnit
        adapter.updateUnit(currentUnit)
        fetchForecast()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}