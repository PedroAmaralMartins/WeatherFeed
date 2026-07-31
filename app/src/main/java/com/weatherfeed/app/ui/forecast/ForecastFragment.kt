package com.weatherfeed.app.ui.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.weatherfeed.app.R
import com.weatherfeed.app.databinding.FragmentForecastBinding
import com.weatherfeed.app.utils.PrefsManager
import kotlinx.coroutines.launch

class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    private val adapter = ForecastAdapter()

    private lateinit var prefsManager: PrefsManager
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentForecastBinding.bind(view)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter

        prefsManager = PrefsManager(requireContext())


        val lat = prefsManager.lastLatitude
        val lon = prefsManager.lastLongitude
        viewModel.loadForecast(lat, lon)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ForecastUiState.Loading -> {
                            binding.progressBar.visibility = view.visibility
                            binding.tvErrorMessage.visibility = View.GONE

                        }

                        is ForecastUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvnextFewDays.visibility = View.VISIBLE
                            binding.tvfiveDayForecast.visibility = View.VISIBLE
                            binding.recyclerview.visibility = View.VISIBLE
                            adapter.submitList(state.days)
                        }

                        is ForecastUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvErrorMessage.visibility = view.visibility
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}