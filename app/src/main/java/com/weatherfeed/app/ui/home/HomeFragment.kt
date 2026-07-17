package com.weatherfeed.app.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.weatherfeed.app.MainActivity
import com.weatherfeed.app.R
import com.weatherfeed.app.data.remote.RetrofitClient
import com.weatherfeed.app.data.repository.WeatherRepository
import com.weatherfeed.app.databinding.FragmentHomeBinding
import com.weatherfeed.app.utils.PrefsManager
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.roundToInt

class HomeFragment : Fragment(R.layout.fragment_home) {
    companion object {
        private const val LOCATION_TIMEOUT_MS = 5000L
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var prefsManager: PrefsManager

    private val viewModel: HomeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            WeatherRepository(RetrofitClient.api)
        )
    }

    private val locationCallback = object : com.google.android.gms.location.LocationCallback() {
        override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
            val lastLocation = locationResult.lastLocation
            if (lastLocation != null) {
                prefsManager.lastLatitude = lastLocation.latitude
                prefsManager.lastLongitude = lastLocation.longitude
                viewModel.loadWeather(lastLocation.latitude, lastLocation.longitude)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            val fineGranted = permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseGranted = permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineGranted || coarseGranted) {
                getLocation()
            } else {
                showPermissionDeniedMessage()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        prefsManager = PrefsManager(requireContext())

        binding.btnRetry.setOnClickListener {
            viewModel.loadWeather(
                prefsManager.lastLatitude,
                prefsManager.lastLongitude
            )
        }
        checkLocationPermission()

        observeViewmodel()
    }


    private fun checkLocationPermission() {
        val hasFine = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            getLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val textError = getString(R.string.location_took_time)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val location = withTimeout(LOCATION_TIMEOUT_MS) {
                    getLastLocation()
                }
                if (location != null) {
                    prefsManager.lastLatitude = location.latitude
                    prefsManager.lastLongitude = location.longitude
                    viewModel.loadWeather(location.latitude, location.longitude)
                } else {
                    requestFreshLocation()
                }
            } catch (e: TimeoutCancellationException) {
                Toast.makeText(
                    requireContext(),
                    textError,
                    Toast.LENGTH_SHORT
                ).show()
                requestFreshLocation()
            } catch (e: Exception) {
                Log.e("HomeFragment", "Erro ao obter localizacao: ${e.message}", e)
                requestFreshLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (continuation.isActive) {
                        continuation.resume(location)
                    }
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }

        }

    private fun showPermissionDeniedMessage() {
        val text = getString(R.string.location_permission_denied)
        if (!isAdded) return
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("MissingPermission")
    private fun requestFreshLocation() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L
        )
            .setMaxUpdates(1)
            .build()

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun mapErrorMessage(throwable: Throwable): String {
        return if (throwable is IOException) {
            getString(R.string.error_network)
        } else {
            getString(R.string.error_loading_weather)
        }
    }

    private fun observeViewmodel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is WeatherUiState.Loading -> {
                            (requireActivity() as MainActivity).showLoading()
                            binding.errorContainer.visibility = View.GONE
                            binding.weatherStatus.visibility = View.GONE
                            binding.tvTemperature.visibility = View.GONE
                            binding.tvCondition.visibility = View.GONE
                            binding.tvFeelsLike.visibility = View.GONE
                            binding.topBar.visibility = View.GONE
                        }

                        is WeatherUiState.Success -> {
                            (requireActivity() as MainActivity).hideLoading()
                            val weather = uiState.data

                            binding.topBar.setLocation(weather.name)
                            binding.tvTemperature.text = getString(
                                R.string.temperature,
                                weather.main.temp.roundToInt()
                            )

                            binding.tvCondition.text =
                                weather.weather.firstOrNull()?.description.orEmpty()

                            binding.tvFeelsLike.text = getString(
                                R.string.tv_feels_like,
                                weather.main.feelsLike.roundToInt()
                            )


                            binding.weatherStatus.setStat1(
                                getString(
                                    R.string.feels_like
                                ),
                                "${weather.main.feelsLike.roundToInt()}°"

                            )

                            binding.weatherStatus.setStat2(
                                getString(
                                    R.string.humidity
                                ),
                                "${weather.main.humidity}%"
                            )
                            binding.weatherStatus.setStat3(
                                getString(
                                    R.string.wind
                                ),
                                "${(weather.wind.speed * 3.6).roundToInt()} km/h"
                            )
                            binding.errorContainer.visibility = View.GONE
                            binding.topBar.visibility = View.VISIBLE
                            binding.tvTemperature.visibility = View.VISIBLE
                            binding.tvCondition.visibility = View.VISIBLE
                            binding.tvFeelsLike.visibility = View.VISIBLE
                            binding.weatherStatus.visibility = View.VISIBLE

                        }

                        is WeatherUiState.Error -> {
                            (requireActivity() as MainActivity).hideLoading()
                            binding.weatherStatus.visibility = View.GONE
                            binding.tvErrorMessage.text = mapErrorMessage(uiState.message)
                            binding.errorContainer.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _binding = null
    }

}

