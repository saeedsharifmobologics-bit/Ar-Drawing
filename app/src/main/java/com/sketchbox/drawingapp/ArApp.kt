package com.sketchbox.drawingapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import coil.Coil
import coil.ImageLoader
import com.google.firebase.FirebaseApp
import com.sketchbox.drawingapp.adsManger.AppOpenManager
import com.sketchbox.drawingapp.adsManger.BillingManager
import com.sketchbox.drawingapp.adsManger.RemoteConfigManager
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.koinModule.appModule
import com.sketchbox.drawingapp.utils.LanguageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.opencv.android.OpenCVLoader

class ArApp : Application() {

    private var appOpenManager: AppOpenManager? = null

    lateinit var billingManager: BillingManager
        private set

    // Use one structured application-level scope
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        // --- Init OpenCV in background ---
        applicationScope.launch {
            OpenCVLoader.initDebug()
        }

        // --- Coil image loader ---
        val imageLoader = ImageLoader.Builder(this)
            .allowHardware(false)
            .build()
        Coil.setImageLoader(imageLoader)

        // --- Language setup ---
        val langCode = LanguageManager.getLanguage(this)
        val locales = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(locales)

        // --- Start Koin ---
        startKoin {
            androidContext(this@ArApp)
            modules(appModule)
        }

        //--- Initialize Billing ---
        billingManager = BillingManager(
            context = this,
            productIds = listOf("sketchbox_weekly", "sketchbox_monthly", "sketchbox_yearly"),
            onProductsFetched = { Utils.productsList = it },
            onPurchaseCompleted = { Utils.subscriptionState = true }
        )

        // --- Launch billing setup ---
        applicationScope.launch(Dispatchers.IO) {
            billingManager.start()

            billingManager.checkActiveSubscription { hasActiveSub ->
                // Switch to Main thread for UI/Ad initialization
                applicationScope.launch(Dispatchers.Main) {
                    Utils.subscriptionState = hasActiveSub
                    if (!hasActiveSub) {
                        initAdMobAndAppOpenAds()
                    }
                }
            }
        }

        // --- Init Firebase & Remote Config ---
        applicationScope.launch {
            initFirebaseAndRemoteConfig()
        }
    }

    private fun initAdMobAndAppOpenAds() {
        /*  MobileAds.initialize(this)
          appOpenManager = AppOpenManager(this)*/
    }

    private suspend fun initFirebaseAndRemoteConfig() {
        // Firebase should be on Main
        withContext(Dispatchers.Main) {

            FirebaseApp.initializeApp(this@ArApp)
        }
        // Remote config on IO
        withContext(Dispatchers.IO) {
            RemoteConfigManager.fetchAndActivate()
        }
    }
}
