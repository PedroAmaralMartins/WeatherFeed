package com.weatherfeed.app.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.weatherfeed.app.R
import com.weatherfeed.app.databinding.FragmentSearchBinding
import com.weatherfeed.app.utils.PrefsManager
import kotlinx.coroutines.launch
import java.io.IOException

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.Factory
    }
    private lateinit var prefsManager: PrefsManager

    private val adapter = SearchAdapter { city ->
        prefsManager.lastLatitude = city.lat
        prefsManager.lastLongitude = city.lon

        findNavController().navigate(R.id.action_SearchFragment_to_HomeFragment)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PrefsManager(requireContext())

        _binding = FragmentSearchBinding.bind(view)

        binding.rvCities.adapter = adapter

        binding.searchBar.setOnTextChanged { text ->
            viewModel.onSearchQueryChanged(text)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is SearchUiState.Idle -> {
                            binding.tvResultsCount.visibility = View.GONE
                            binding.rvCities.visibility = View.GONE
                            adapter.submitList(emptyList())
                        }

                        is SearchUiState.Loading -> {
                            binding.tvResultsCount.visibility = View.GONE
                            binding.rvCities.visibility = View.GONE
                        }

                        is SearchUiState.Empty -> {
                            binding.tvResultsCount.visibility = View.VISIBLE
                            binding.tvResultsCount.text = getString(R.string.search_no_results)
                            binding.rvCities.visibility = View.GONE
                            adapter.submitList(emptyList())
                        }

                        is SearchUiState.Success -> {
                            binding.tvResultsCount.visibility = View.VISIBLE
                            binding.tvResultsCount.text = resources.getQuantityString(
                                R.plurals.search_results_count,
                                state.cities.size,
                                state.cities.size

                            )
                            binding.rvCities.visibility = View.VISIBLE
                            adapter.submitList(state.cities)
                        }

                        is SearchUiState.Error -> {
                            binding.tvResultsCount.visibility = View.VISIBLE
                            binding.tvResultsCount.text = mapErrorMessage(state.throwable)
                            binding.rvCities.visibility = View.GONE
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
            getString(R.string.error_loading_city)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}