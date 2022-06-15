package com.example.ambulanceondemand.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.ambulanceondemand.databinding.ActivitySplashBinding
import com.example.ambulanceondemand.ui.landing.MapsActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    companion object {
        private const val SPLASH_DELAY_LENGTH : Long = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        waitForWhile()
    }

    private fun waitForWhile() {
        Handler(Looper.getMainLooper()).postDelayed({
            callNextActivity()
        }, SPLASH_DELAY_LENGTH)

    }

    private fun callNextActivity() {
        val intent = Intent(this, MapsActivity :: class.java)
        startActivity(intent)
        finish()
    }
}