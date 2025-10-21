package com.example.ardrawing.onBoardingScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ardrawing.R
import com.example.ardrawing.adsManger.SplashAppOpenAdManager
import com.example.ardrawing.utils.ArDrawingSharePreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var arDrawingSharePreference: ArDrawingSharePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        arDrawingSharePreference = ArDrawingSharePreference(this)

        // 🔹 Load splash ad as soon as splash starts
        SplashAppOpenAdManager.loadSplashAd(this)

        lifecycleScope.launch {
            delay(6000) // 7 seconds delay

            SplashAppOpenAdManager.showSplashAdIfAvailable(this@SplashActivity) {
                goNextScreen()
            }
        }
    }

    private fun goNextScreen() {
        if (arDrawingSharePreference.isFirstLaunch(this)) {
            startActivity(Intent(this, LanguageActivity::class.java))
        } else {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        finish()
    }
}
