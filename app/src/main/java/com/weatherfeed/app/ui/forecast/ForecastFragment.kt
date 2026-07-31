package com.weatherfeed.app.ui.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherfeed.app.databinding.FragmentForecastBinding
import com.weatherfeed.app.utils.PrefsManager
import kotlinx.coroutines.launch

class ForecastFragment : Fragment() {
    private lateinit var adapter: ForecastAdapter

    private lateinit var prefsManager: PrefsManager
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentForecastBinding.bind(view)

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter

        val lat = prefsManager.lastLatitude
        val lon = prefsManager.lastLongitude
        viewModel.loadForecast(lat, lon)//TODO() I will create the viewmodel in the next branch.


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    //TODO() It will be implemented after the ViewModel is created.

                    is ForecastUiState.Loading -> {
                        binding.progressBar.visibility =  view.visibility

                    }

                    is ForecastUiState.Success -> {
                        adapter.updateItems(state.days)
                    }

                    is ForecastUiState.Error -> {
                        binding.tvErrorMessage.visibility = view.visibility
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}