package com.sketchbox.drawingapp.adsManger

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.sketchbox.drawingapp.BuildConfig
import com.sketchbox.drawingapp.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

object adsUtils {

    private var mInterstitialAd: InterstitialAd? = null
    private var currentNativeAd: NativeAd? = null

    fun loadNativeAd(rootView: View, context: Context) {

        disposeNativeAd()
        val adContainer = rootView.findViewById<FrameLayout>(R.id.ads_container)
        adContainer.removeAllViews()
        adContainer.visibility = View.VISIBLE

        // Inflate the layout containing shimmer + native ad view
        val adLayout =
            LayoutInflater.from(context).inflate(R.layout.native_ads_layout, adContainer, false)

        val shimmer = adLayout.findViewById<ShimmerFrameLayout>(R.id.shimmer_container)
        val nativeAdView = adLayout.findViewById<NativeAdView>(R.id.native_ad_view)

        // Add inflated layout to the ad container
        adContainer.addView(adLayout)

        // Start shimmer animation, show shimmer and hide native ad view initially
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()
        nativeAdView.visibility = View.INVISIBLE

        val nativeAdKey = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1044960115" else RemoteConfigManager.getStringValue(
                "NATIVE_AD_KEY"
            )

        // Setup AdLoader with native ad callback
        val adLoader =
            AdLoader.Builder(context, nativeAdKey)  // <-- Replace with your real ad unit ID
                .forNativeAd { nativeAd ->
                    disposeNativeAd()

                    // Populate native ad view with data
                    populateNativeAdView(nativeAd, nativeAdView)

                    // Stop shimmer, hide shimmer layout and show native ad view
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    nativeAdView.visibility = View.VISIBLE
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e("NativeAd", "Ad failed to load: ${adError.message}")

                        // Stop shimmer and hide entire ad container on failure
                        /*   shimmer.stopShimmer()
                           shimmer.visibility = View.GONE
                           adContainer.visibility = View.GONE*/
                    }
                })
                .build()

        // Load the ad
        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
        val bodyView = adView.findViewById<TextView>(R.id.ad_body)
        val adsImage = adView.findViewById<ImageView>(R.id.adsimageView)
        val callToActionView = adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)

        headlineView.text = nativeAd.headline
        bodyView.text = nativeAd.body

        val images = nativeAd.images
        if (images.isNotEmpty() && images[0].drawable != null) {
            adsImage.setImageDrawable(images[0].drawable)
        }
        callToActionView.text = nativeAd.callToAction
        adView.headlineView = headlineView
        adView.bodyView = bodyView
        adView.callToActionView = callToActionView

        adView.setNativeAd(nativeAd)
    }


    //Ye function Native Ad ko safely dispose argv
    fun disposeNativeAd() {
        currentNativeAd?.destroy()
        currentNativeAd = null
        Log.d("NativeAd", "Native ad disposed successfully")
    }


    fun loadBannerAd(rootView: View, context: Context) {
        val adContainer = rootView.findViewById<FrameLayout>(R.id.bannerAdView)
        adContainer.removeAllViews()
        // Create a new AdView
        val bannerAdKey =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111" else RemoteConfigManager.getStringValue(
                "BANNER_AD_KEY"
            )

        val adView = AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = bannerAdKey // Test ID
        }

        adContainer.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("BannerAd", "Banner loaded")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("BannerAd", "Failed to load: ${adError.message}")
            }
        }
    }


    fun preloadInterstitialAd(activity: Activity) {
        val interstitialAdKey =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else RemoteConfigManager.getStringValue(
                "INTERSTITIAL_AD_KEY"
            )

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
            interstitialAdKey,  // test ID
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    mInterstitialAd = ad
                }
            }
        )
    }

    fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    preloadInterstitialAd(activity) // next ad load kardo background me
                    onAdClosed.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    mInterstitialAd = null
                    preloadInterstitialAd(activity)
                    onAdClosed.invoke()
                }
            }
            mInterstitialAd?.show(activity)
        } else {
            // agar ad ready nahi hai to seedha callback
            onAdClosed.invoke()
            preloadInterstitialAd(activity)
        }
    }


}