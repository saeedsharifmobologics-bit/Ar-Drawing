package com.sketchbox.drawingapp.adsManger

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.sketchbox.drawingapp.BuildConfig

object SplashAppOpenAdManager {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false

    private var timeoutHandler: Handler? = null
    private var timeoutRunnable: Runnable? = null

    /** 🔁 External callback to notify SplashActivity about ad state */
    var adEventCallback: ((AdEvent) -> Unit)? = null

    /** Load the splash ad */
    fun loadSplashAd(context: Context) {
        if (isLoadingAd || appOpenAd != null) return

        val adUnitId = if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/9257395921" // test ad unit
        } else {
            RemoteConfigManager.getStringValue("APP_OPEN_AD_SPLASH")
        }

        isLoadingAd = true
        adEventCallback?.invoke(AdEvent.AdLoading)

        val adRequest = AdRequest.Builder().build()

        Log.d("SplashAd", "📡 Loading Splash AppOpenAd...")

        AppOpenAd.load(
            context,
            adUnitId,
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d("SplashAd", "✅ Splash Ad Loaded")
                    appOpenAd = ad
                    isLoadingAd = false
                    adEventCallback?.invoke(AdEvent.AdLoaded)

                    startTimeout() // Start timeout for show
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("SplashAd", "❌ Failed to load Splash Ad: ${error.message}")
                    isLoadingAd = false
                    appOpenAd = null
                    adEventCallback?.invoke(AdEvent.AdFailedToLoad(error))
                }
            }
        )
    }

    /** ✅ Show the splash ad if available */
    fun showSplashAdIfAvailable(activity: Activity, onAdDismissed: () -> Unit) {
        val ad = appOpenAd
        if (ad != null) {
            cancelTimeout()

            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("SplashAd", "Splash Ad dismissed")
                    appOpenAd = null
                    adEventCallback?.invoke(AdEvent.AdDismissed)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("SplashAd", "Failed to show Splash Ad: ${adError.message}")
                    appOpenAd = null
                    adEventCallback?.invoke(AdEvent.AdFailedToShow(adError))
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("SplashAd", "Splash Ad is showing")
                }
            }

            ad.show(activity)
        } else {
            Log.d("SplashAd", "No Splash Ad available to show")
            adEventCallback?.invoke(AdEvent.AdNotAvailable)
            onAdDismissed()
        }
    }

    /**Timeout fallback (in case ad is loaded but never shown) */
    private fun startTimeout(delayMs: Long = 10_000L) {
        timeoutHandler = Handler(Looper.getMainLooper())
        timeoutRunnable = Runnable {
            if (appOpenAd != null) {
                Log.w("SplashAd", "Ad show timeout reached")
                appOpenAd = null
                adEventCallback?.invoke(AdEvent.AdTimeout)
            }
        }
        timeoutHandler?.postDelayed(timeoutRunnable!!, delayMs)
    }

    private fun cancelTimeout() {
        timeoutHandler?.removeCallbacks(timeoutRunnable!!)
        timeoutHandler = null
        timeoutRunnable = null
    }

    sealed class AdEvent {
        object AdLoading : AdEvent()
        object AdLoaded : AdEvent()
        object AdDismissed : AdEvent()
        object AdNotAvailable : AdEvent()
        object AdTimeout : AdEvent()
        class AdFailedToLoad(val error: LoadAdError) : AdEvent()
        class AdFailedToShow(val error: AdError) : AdEvent()
        object NoInternet : AdEvent() // optional, if you want to support
    }
}
