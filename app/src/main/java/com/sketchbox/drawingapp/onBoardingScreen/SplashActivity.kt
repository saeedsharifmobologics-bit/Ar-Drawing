package com.sketchbox.drawingapp.onBoardingScreen

import android.content.Intent
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.ump.*
import com.sketchbox.drawingapp.BuildConfig
import com.sketchbox.drawingapp.MainActivity
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adsManger.SplashAppOpenAdManager
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.utils.ArDrawingSharePreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var arDrawingSharePreference: ArDrawingSharePreference

    private var isConsentNeeded = false
    private var isConsentFormDismissed = false
    private var isAdLoaded = false
    private var isAdDismissed = false
    private var isAdFailed = false
    private var didNavigate = false
    private var isConsentHandled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        arDrawingSharePreference = ArDrawingSharePreference(this)

        // 🟡 Simulate subscription state from saved prefs (replace with your real logic)
        val isUserSubscribed = Utils.subscriptionState // Replace with real logic

        // 🟡 Load splash ad
        SplashAppOpenAdManager.loadSplashAd(this)

        // 🟡 UMP Consent setup
        setupUMPConsent { consentNeeded ->
            isConsentNeeded = consentNeeded
            if (!isConsentNeeded) {
                isConsentFormDismissed = true
            }
            isConsentHandled = true
        }

        // 🟡 Listen for ad events
        SplashAppOpenAdManager.adEventCallback = { event ->
            runOnUiThread {
                when (event) {
                    is SplashAppOpenAdManager.AdEvent.AdLoaded -> {
                        isAdLoaded = true
                    }
                    is SplashAppOpenAdManager.AdEvent.AdDismissed -> {
                        isAdDismissed = true
                        tryNavigate()
                    }
                    is SplashAppOpenAdManager.AdEvent.AdFailedToShow,
                    is SplashAppOpenAdManager.AdEvent.AdTimeout,
                    is SplashAppOpenAdManager.AdEvent.AdFailedToLoad -> {
                        isAdFailed = true
                        tryNavigate()
                    }
                    is SplashAppOpenAdManager.AdEvent.NoInternet -> {
                        lifecycleScope.launch {
                            delay(3500)
                            isAdFailed = true
                            tryNavigate()
                        }
                    }
                    else -> Unit
                }
            }
        }

        // 🟡 Loop until everything is ready or timeout
        lifecycleScope.launch {
            val timeoutMs = 12_000L
            val stepMs = 100L
            val deadline = SystemClock.elapsedRealtime() + timeoutMs

            while (SystemClock.elapsedRealtime() < deadline) {
                val consentOk = !isConsentNeeded || isConsentFormDismissed

                if (!consentOk) {
                    delay(stepMs)
                    continue
                }

                if (isUserSubscribed) {
                    delay(3500)
                    tryNavigate()
                    return@launch
                }

                if (isAdDismissed || isAdFailed) {
                    tryNavigate()
                    return@launch
                }

                if (isAdLoaded) {
                    SplashAppOpenAdManager.showSplashAdIfAvailable(this@SplashActivity) {
                        tryNavigate()
                    }
                    return@launch
                }

                delay(stepMs)
            }

            // Fallback: Force navigation after timeout
            if (!didNavigate) {
                tryNavigate()
            }
        }
    }

    private fun tryNavigate() {
        if (didNavigate) return
        didNavigate = true

        val intent = if (arDrawingSharePreference.isFirstLaunch(this)) {
            Intent(this, LanguageActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun setupUMPConsent(callback: (isConsentNeeded: Boolean) -> Unit) {
        val paramsBuilder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)

        if (BuildConfig.DEBUG) {
            val debugSettings = ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("F2CA8BFE17FE2211C369B5607091B2ED") // Replace with your own test ID
                .build()
            paramsBuilder.setConsentDebugSettings(debugSettings)
        }

        val consentInformation = UserMessagingPlatform.getConsentInformation(this)

        consentInformation.requestConsentInfoUpdate(
            this,
            paramsBuilder.build(),
            {
                if (consentInformation.isConsentFormAvailable) {
                    loadConsentForm(consentInformation)
                    callback(true)
                } else {
                    callback(false)
                }
            },
            { formError ->
                Log.e("UMP", "Consent info error: ${formError.message}")
                callback(false)
            }
        )
    }

    private fun loadConsentForm(consentInformation: ConsentInformation) {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(this) {
                        isConsentFormDismissed = true
                    }
                } else {
                    isConsentFormDismissed = true
                }
            },
            { formError ->
                Log.e("UMP", "Consent form error: ${formError.message}")
                isConsentFormDismissed = true
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        SplashAppOpenAdManager.adEventCallback = null
    }
}
