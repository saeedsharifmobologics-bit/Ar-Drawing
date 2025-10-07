package com.example.ardrawing

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.ardrawing.koinModule.appModule
import com.example.ardrawing.utils.LanguageManager
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ArApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val langCode = LanguageManager.getLanguage(this)
        val locales = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(locales)
// Initialize AdMob
        MobileAds.initialize(this)
        startKoin {
            androidContext(this@ArApp)
            modules(appModule)
        }
    }
}