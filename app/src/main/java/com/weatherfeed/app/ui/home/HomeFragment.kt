package com.weatherfeed.app.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.weatherfeed.app.R
import com.weatherfeed.app.utils.PrefsManager
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HomeFragment : Fragment(R.layout.fragment_home) {
    companion object {
        private const val LOCATION_TIMEOUT_MS = 5000L
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var prefsManager: PrefsManager

    private val viewModel: HomeViewModel by viewModels()

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
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getLocation()
            } else {
                showPermissionDeniedMessage()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        prefsManager = PrefsManager(requireContext())

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            getLocation()
        } else {
            locationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
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
                println("Erro ao obter localização : ${e.message}")
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

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates { locationCallback }
    }
}

