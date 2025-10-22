package com.sketchbox.drawingapp.adsManger

import android.annotation.SuppressLint
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

object RemoteConfigManager {

    @SuppressLint("StaticFieldLeak")
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val defaults = mapOf(
            "APP_OPEN_AD" to "",
            "BANNER_AD_KEY" to "",
            "INTERSTITIAL_AD_KEY" to "",
            "NATIVE_AD_KEY" to "",
            "APP_OPEN_AD_SPLASH" to "",
        )

        remoteConfig.setDefaultsAsync(defaults)

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getIntValue(key: String): Int {
        return remoteConfig.getDouble(key).toInt()
    }

    fun getStringValue(key: String): String {
        return remoteConfig.getString(key)
    }
}

