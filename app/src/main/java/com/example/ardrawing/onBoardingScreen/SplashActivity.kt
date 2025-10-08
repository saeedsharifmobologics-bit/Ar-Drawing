package com.example.ardrawing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ardrawing.onBoardingScreen.LanguageActivity
import com.example.ardrawing.onBoardingScreen.OnboardingActivity
import com.example.ardrawing.utils.AppLaunchPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(4000) // 4000ms = 4 seconds

            if (AppLaunchPrefs.isFirstLaunch(this@SplashActivity)){
                startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
                finish() // close splash so it can't be returned to
            }else{
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                finish() // close splash so it can't be returned to
            }

        }
    }
}
