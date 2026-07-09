package com.weatherfeed.app.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.weatherfeed.app.MainActivity
import com.weatherfeed.app.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DURATION_MS = 2000L
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            startAnimations()
        } else {
            navigatieToMain()
        }

    }

    private fun startAnimations() {
        binding.ivSplashLogo.alpha = 0f
        binding.titleFirstScreen.alpha = 0f

        binding.ivSplashLogo.animate().alpha(1f).setDuration(800).start()
        binding.titleFirstScreen.animate().alpha(1f).setStartDelay(400).setDuration(600).start()

        lifecycleScope.launch {
            delay(SPLASH_DURATION_MS)
            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java)
            )
            finish()
        }
    }

    private fun navigatieToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}