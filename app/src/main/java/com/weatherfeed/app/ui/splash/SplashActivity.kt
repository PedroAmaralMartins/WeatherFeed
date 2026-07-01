package com.weatherfeed.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.weatherfeed.app.MainActivity
import com.weatherfeed.app.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.icFirstScreen.alpha = 0f
        binding.titleFirstScreen.alpha = 0f
        binding.icFirstScreen.animate().alpha(1f).setDuration(800).start()
        binding.titleFirstScreen.animate().alpha(1f).setStartDelay(400).setDuration(600).start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2200)

    }
}