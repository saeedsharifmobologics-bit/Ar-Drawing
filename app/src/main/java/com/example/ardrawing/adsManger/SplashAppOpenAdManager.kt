package com.example.ardrawing.adsManger

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.ardrawing.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

object SplashAppOpenAdManager {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false

    //  Load Splash Ad
    fun loadSplashAd(context: Context) {
        if (isLoadingAd || appOpenAd != null) return
        val appOpenAdKey = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else RemoteConfigManager.getStringValue("APP_OPEN_AD_SPLASH")

        isLoadingAd = true
        val adRequest = AdRequest.Builder().build()

        Log.d("SplashAd", "📡 Loading Splash AppOpenAd...")

        AppOpenAd.load(
            context,
            appOpenAdKey, // ✅ your splash ad unit id (test or real)
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d("SplashAd", "✅ Splash AppOpenAd Loaded")
                    appOpenAd = ad
                    isLoadingAd = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("SplashAd", "❌ Failed to load Splash Ad: ${error.message}")
                    isLoadingAd = false
                    appOpenAd = null
                }
            }
        )
    }

    // ✅ Show Splash Ad
    fun showSplashAdIfAvailable(activity: Activity, onAdDismissed: () -> Unit) {
        val ad = appOpenAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("SplashAd", "👋 Splash Ad dismissed")
                    appOpenAd = null
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("SplashAd", "❌ Failed to show Splash Ad: ${adError.message}")
                    appOpenAd = null
                    onAdDismissed()
                }
            }
            ad.show(activity)
        } else {
            Log.d("SplashAd", "⚠️ No Splash Ad available to show")
            onAdDismissed()
        }
    }
}
