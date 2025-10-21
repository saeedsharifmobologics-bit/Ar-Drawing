// com.example.ardrawing.howtouse.LanguageManager.kt
package com.example.ardrawing.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat

object LanguageManager {
    private const val PREF_NAME = "settings"
    private const val KEY_LANG = "app_lang"

    var selectedLang: String = ""

    fun setLanguage(context: Context, langCode: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit { putString(KEY_LANG, langCode) }

        val locales = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    fun getLanguage(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_LANG, "en") ?: "en"
    }
}



