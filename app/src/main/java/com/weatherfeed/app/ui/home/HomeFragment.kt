package com.weatherfeed.app.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.weatherfeed.app.R

class HomeFragment : Fragment(R.layout.fragment_home) {


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
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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

    private fun getLocation() {
        //TODO: implementar com fusedLocationProviderClient.lastLocation
        }


    private fun showPermissionDeniedMessage() {
        Toast.makeText(
            requireContext(),
            "Permissão de localização negada",
            Toast.LENGTH_SHORT
        ).show()
    }
}
