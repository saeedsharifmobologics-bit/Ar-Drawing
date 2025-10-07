package com.example.ardrawing.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlin.collections.filter
import kotlin.collections.isNotEmpty
import kotlin.collections.toTypedArray


object ImageHolder {
    var bitmap: Bitmap? = null
    var pickLocation: String? = null
}
class PermissionHandler(
    private val context: Context,
    private val multiplePermissionLauncher: ActivityResultLauncher<Array<String>>
) {

    private val requiredPermissions: Array<String>
        get() {
            return when {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU -> arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.RECORD_AUDIO
                )

                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q -> arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )

                else -> arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
            }
        }


    fun requestPermission() {
        val permissionsToRequest = requiredPermissions.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            multiplePermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            Toast.makeText(context, "All permissions already granted ", Toast.LENGTH_SHORT).show()
        }
    }

    fun showRetryDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("You need to grant permissions for proper functionality.")
            .setCancelable(false)
            .setPositiveButton("Try Again") { _, _ ->
                requestPermission()
            }
            .show()
    }

    fun showSettingsDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("You have denied permissions multiple times. Please enable them from Settings.")
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }
}
