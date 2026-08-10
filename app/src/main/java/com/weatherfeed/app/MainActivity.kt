package com.weatherfeed.app

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.weatherfeed.app.databinding.ActivityMainBinding
import androidx.navigation.navOptions


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        configureNavigation()
    }

    private fun navigateTo(destinationId: Int) {
        val options = navOptions {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        navController.navigate(destinationId, null, options)
    }

    private fun configureNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigationView.setOnItemSelected { index ->
            when (index) {
                0 -> navigateTo(R.id.homeFragment)
                1 -> navigateTo(R.id.forecastFragment)
                2 -> navigateTo(R.id.searchFragment)
                3 -> navigateTo(R.id.settingsFragment)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val index = when (destination.id) {
                R.id.homeFragment -> 0
                R.id.forecastFragment -> 1
                R.id.searchFragment -> 2
                R.id.settingsFragment -> 3
                else -> return@addOnDestinationChangedListener
            }

            binding.bottomNavigationView.setSelectedIndex(index)
        }
    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }
}