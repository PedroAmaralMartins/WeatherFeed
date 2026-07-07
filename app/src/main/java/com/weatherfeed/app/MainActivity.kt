package com.weatherfeed.app

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.weatherfeed.app.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.splashLayout.tvSplashLogo.alpha = 0f
        binding.splashLayout.titleFirstScreen.alpha = 0f

        binding.splashLayout.tvSplashLogo.animate().alpha(1f).setDuration(800).start()
        binding.splashLayout.titleFirstScreen.animate().alpha(1f).setStartDelay(400).setDuration(600).start()

        lifecycleScope.launch {
            delay(2000)

            binding.bottomNavigationView.visibility = View.VISIBLE
            binding.bottomNavigationView.alpha = 0f
            binding.bottomNavigationView.animate()
                .alpha(1f)
                .setDuration(500)
                .start()

            binding.splashLayout.root.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction {
                    binding.splashLayout.root.visibility = View.GONE
                }.start()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
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

    private fun configureNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }

}