package com.weatherfeed.app.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.weatherfeed.app.MainActivity
import com.weatherfeed.app.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvSplashLogo.alpha = 0f
        binding.titleFirstScreen.alpha = 0f
        binding.tvSplashLogo.animate().alpha(1f).setDuration(800).start()
        binding.titleFirstScreen.animate().alpha(1f).setStartDelay(400).setDuration(600).start()

        lifecycleScope.launch {
            finish()
        }
    }
}