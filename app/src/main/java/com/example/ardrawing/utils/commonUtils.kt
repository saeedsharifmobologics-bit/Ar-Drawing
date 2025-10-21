package com.example.ardrawing.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.File
import androidx.core.graphics.createBitmap
import com.example.ardrawing.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import okio.Inflater

object CommonUtils {


    object ImageHolder {
        var bitmap: Bitmap? = null
        var pickLocation: String? = null
    }
    fun registerGalleryPicker(
        activityResultCaller: ActivityResultCaller,  // This can be Activity or Fragment
        onImagePicked: (Uri?) -> Unit
    ): ActivityResultLauncher<String> {

        return activityResultCaller.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            onImagePicked(uri)
        }
    }

    fun pickImageFromGallery(launcher: ActivityResultLauncher<String>) {
        launcher.launch("image/*")
    }


    fun createImageUri(context: Context): Uri? {
        val image = File(context.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(context, "com.example.ardrawing.fileProvider", image)
    }


    /*
                suspend fun applySketchOverlayInBackground(intensity: Float): Bitmap =
        withContext(Dispatchers.Default) {
            val gray = Mat()
            val inv = Mat()
            val blur = Mat()
            val invertedBlur = Mat()
            val colorMat = Mat()
            val sketchBGR = Mat()
            val output = Mat()
            var sketchMask: Mat? = null  // fix for release later
            val resultBitmap: Bitmap

            try {
                // Step 1: Copy the original image
                CommonUtils.srcMat.copyTo(colorMat)

                // Step 2: Convert to grayscale
                Imgproc.cvtColor(colorMat, gray, Imgproc.COLOR_BGR2GRAY)

                // Step 3: Invert grayscale
                Core.bitwise_not(gray, inv)

                // Step 4: Apply Gaussian blur to the inverted image (smaller kernel for sharpness)
                val kSize = 21  // sharper edges than 15
                Imgproc.GaussianBlur(inv, blur, Size(kSize.toDouble(), kSize.toDouble()), 0.0)

                // Step 5: Invert the blurred image
                Core.bitwise_not(blur, invertedBlur)

                // Step 6: Dodge blend (gray / invertedBlur)
                sketchMask = Mat()
                Core.divide(gray, invertedBlur, sketchMask, 255.0)


                // Step 7: Convert sketch to BGR so we can blend with colorMat
                Imgproc.cvtColor(sketchMask, sketchBGR, Imgproc.COLOR_GRAY2BGR)

                // Step 8: Resize if needed
                if (sketchBGR.size() != colorMat.size()) {
                    Imgproc.resize(sketchBGR, sketchBGR, colorMat.size())
                }

                // Step 9: Handle RGBA input
                if (colorMat.channels() == 4) {
                    Imgproc.cvtColor(colorMat, colorMat, Imgproc.COLOR_RGBA2BGR)
                }

                // Step 10: Blend the original image with the sketch
                Core.addWeighted(
                    colorMat,
                    1.0 - intensity,
                    sketchBGR,
                    intensity.toDouble(),
                    0.0,
                    output
                )

                // Step 11: Convert result to Bitmap
                resultBitmap = createBitmap(output.cols(), output.rows())
                Utils.matToBitmap(output, resultBitmap)
            } finally {
                // Step 12: Release Mats safely
                gray.release()
                inv.release()
                blur.release()
                invertedBlur.release()
                sketchMask?.release()
                sketchBGR.release()
                colorMat.release()
                output.release()
            }

            resultBitmap
        }

    */


    fun shareApp(context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "SketchBox -AR Drawing App: https://play.google.com/store/apps/details?id=${context.packageName}"
            )
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }





    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }






    fun getRotateClockwiseAnimation(context: Context): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.rotate_clockwise)
    }

    fun getRotateAnticlockwiseAnimation(context: Context): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.rotate_anti_clockwise)
    }
}
