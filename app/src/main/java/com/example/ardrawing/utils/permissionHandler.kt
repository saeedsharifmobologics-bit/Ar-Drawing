package com.example.ardrawing.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat


object ImageHolder {
    var bitmap: Bitmap? = null
    var pickLocation: String? = null
}

class PermissionHandler(
    private val context: Context,
    private val multiplePermissionLauncher: ActivityResultLauncher<String?>
) {


    fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission() {
        multiplePermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun isReadMediaImagesGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestReadMediaImagesPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            multiplePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            multiplePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun requestAudioPermission() {
        multiplePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    fun isRequestAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    /*
        fun requestPermission() {
            val permissionsToRequest = requiredPermissions.filter { permission ->
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            }

            if (permissionsToRequest.isNotEmpty()) {
                multiplePermissionLauncher.launch(permissionsToRequest.toTypedArray())
            } else {
                Toast.makeText(context, "All permissions already granted ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    */

    fun showRetryDialog(permissionName: String) {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("You need to grant permissions for proper functionality.")
            .setCancelable(false)
            .setPositiveButton("Try Again") { _, _ ->
               when(permissionName){
                   "AudioPermission" -> requestAudioPermission()
                   "GalleryPermission" -> requestReadMediaImagesPermission()
                   "CameraPermission" -> requestCameraPermission()
               }
            }
            .show()
    }

    fun showSettingsDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("You have denied permission multiple times. Please enable them from Settings.")
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
