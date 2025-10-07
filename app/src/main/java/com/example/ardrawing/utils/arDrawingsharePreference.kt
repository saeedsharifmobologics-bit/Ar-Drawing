package com.example.ardrawing.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ArDrawingSharePreference(context: Context) {

    private val PREFS_NAME = "ar_drawing_prefs"
    private val KEY_INT_VALUE = "key_int_value"

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Save integer
    fun saveInt(value: Int) {
        prefs.edit { putInt(KEY_INT_VALUE, value) }
    }

    // Get integer (default = 0)
    fun getInt(): Int {
        return prefs.getInt(KEY_INT_VALUE, 0)
    }

    // Optional: clear value
    fun clear() {
        prefs.edit().remove(KEY_INT_VALUE).apply()
    }
}
