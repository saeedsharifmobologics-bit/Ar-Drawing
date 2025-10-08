package com.example.ardrawing.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ArDrawingSharePreference(context: Context) {

    private val PREFS_NAME = "ar_drawing_prefs"

    // Alag keys for different permissions
    private val KEY_CAMERA_PERMISSION_COUNT = "key_camera_permission_count"
    private val KEY_READ_STORAGE_PERMISSION_COUNT = "key_read_storage_permission_count"
    private val KEY_AUDIO_PERMISSION_COUNT = "key_audio_permission_count"

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Save camera permission request count
    fun saveCameraPermissionCount(value: Int) {
        prefs.edit { putInt(KEY_CAMERA_PERMISSION_COUNT, value) }
    }

    fun getCameraPermissionCount(): Int {
        return prefs.getInt(KEY_CAMERA_PERMISSION_COUNT, 0)
    }

    // Save read storage permission request count
    fun saveReadStoragePermissionCount(value: Int) {
        prefs.edit { putInt(KEY_READ_STORAGE_PERMISSION_COUNT, value) }
    }

    fun getReadStoragePermissionCount(): Int {
        return prefs.getInt(KEY_READ_STORAGE_PERMISSION_COUNT, 0)
    }

    // Save audio permission request count
    fun saveAudioPermissionCount(value: Int) {
        prefs.edit { putInt(KEY_AUDIO_PERMISSION_COUNT, value) }
    }

    fun getAudioPermissionCount(): Int {
        return prefs.getInt(KEY_AUDIO_PERMISSION_COUNT, 0)
    }

    // Optional: clear all counts
    fun clearAll() {
        prefs.edit {
            remove(KEY_CAMERA_PERMISSION_COUNT)
            remove(KEY_READ_STORAGE_PERMISSION_COUNT)
            remove(KEY_AUDIO_PERMISSION_COUNT)
        }
    }
}
