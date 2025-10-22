package com.sketchbox.drawingapp.adsManger

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.sketchbox.drawingapp.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.sketchbox.drawingapp.fragments.CameraLauncher
import com.sketchbox.drawingapp.utils.LanguageManager


class AppOpenManager(private val myApplication: Application) :
    Application.ActivityLifecycleCallbacks {

    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isAdShowing = false
    private var isLoadingAd = false

    // 👇 App ka background/foreground track karne ke liye
    private var startedActivityCount = 1
    private var isAppInBackground = false

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        loadAd()
    }

    private fun loadAd() {
        if (isLoadingAd || appOpenAd != null) return

        Log.d("AppOpenManager", "🔄 Loading AppOpenAd...")
        isLoadingAd = true
        val appOpenAdKey =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else RemoteConfigManager.getStringValue(
                "APP_OPEN_AD"
            )
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            myApplication,
            appOpenAdKey, // test ID
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d("AppOpenManager", "✅ Ad loaded")
                    appOpenAd = ad
                    isLoadingAd = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AppOpenManager", "Failed to load ad: ${error.message}")
                    isLoadingAd = false
                }
            }
        )
    }

    private fun showAdIfAvailable() {
        if (isAdShowing || appOpenAd == null) {
            loadAd()
            return
        }

        if (LanguageManager.isLanguageChanging || CameraLauncher.isCameraFeatureActive
        ) {
            LanguageManager.isLanguageChanging = false
            CameraLauncher.isCameraFeatureActive = false
            return
        }

        currentActivity?.let { activity ->
            Log.d("AppOpenManager", "Showing AppOpenAd...")

            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AppOpenManager", "Ad dismissed — reloading...")
                    isAdShowing = false
                    appOpenAd = null
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    isAdShowing = false
                    appOpenAd = null
                    loadAd()
                }

                override fun onAdShowedFullScreenContent() {
                    isAdShowing = true
                }
            }

            appOpenAd?.show(activity)
        } ?: Log.w("AppOpenManager", "currentActivity null — can't show ad")
    }

    // 🧭 Foreground/background detect yahin se hoga
    override fun onActivityStarted(activity: Activity) {
        startedActivityCount++
        if (isAppInBackground && startedActivityCount > 0) {
            Log.d("AppOpenManager", "App foreground me wapas aaya")
            isAppInBackground = false
            showAdIfAvailable()
        }
    }

    override fun onActivityStopped(activity: Activity) {
        startedActivityCount--
        if (startedActivityCount == 0) {
            isAppInBackground = true
        }
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        Log.d("AppOpenManager", "resume call hogaya ")
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
