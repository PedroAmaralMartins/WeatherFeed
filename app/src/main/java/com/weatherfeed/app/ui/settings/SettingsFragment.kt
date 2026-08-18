package com.weatherfeed.app.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.weather.designsystem.xml.WeatherTemperatureToggleView
import com.weatherfeed.app.R
import com.weatherfeed.app.databinding.FragmentSettingsBinding
import com.weatherfeed.app.utils.PrefsManager

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private lateinit var prefsManager: PrefsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingsBinding.bind(view)
        prefsManager = PrefsManager(requireContext())

        setupTemperatureRow()
    }

    private fun setupTemperatureRow() {
        val toggle = WeatherTemperatureToggleView(requireContext())
        toggle.setUnit(isCelsius = prefsManager.isCelsius)
        toggle.setOnUnitChanged { isCelsius ->
            prefsManager.isCelsius = isCelsius
            toggle.setUnit(isCelsius)
        }
        binding.rowUnit.setTrailing(toggle)

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}