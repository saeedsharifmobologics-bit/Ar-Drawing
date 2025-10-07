import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.camera.core.*
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.ardrawing.R
import com.example.ardrawing.buinesslogiclayer.ArDrawingViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

object CameraPreviewUtils {



    fun startCamera(
        fragment: Fragment,
        previewView: PreviewView,
        viewModel: ArDrawingViewmodel,
        lifecycleOwner: LifecycleOwner,
        onImageCaptured: (Bitmap) -> Unit = {}
    ): Pair<ImageCapture, VideoCapture<Recorder>> {

        val imageCapture = ImageCapture.Builder().build()

        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()

        val videoCapture = VideoCapture.withOutput(recorder)

        // Call getCameraProvider with callback
        viewModel.getCameraProvider(fragment.requireContext()) { cameraProvider ->

            // Unbind any use-cases before rebinding
            cameraProvider.unbindAll()

            // Build Preview use-case
            val preview = Preview.Builder().build().also {
                // setSurfaceProvider must be called on main thread
                fragment.requireActivity().runOnUiThread {
                    it.surfaceProvider = previewView.surfaceProvider
                }
            }

            // Build ImageAnalysis use-case
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                        try {
                            val bitmap = imageProxy.toBitmap()
                            onImageCaptured(bitmap)
                        } catch (e: Exception) {
                            Log.e("CameraX", "Image analysis failed: ${e.message}", e)
                        } finally {
                            imageProxy.close()
                        }
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Bind use-cases to lifecycle on main thread
            fragment.requireActivity().runOnUiThread {
                try {
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture,
                        videoCapture,
                        imageAnalyzer
                    )
                } catch (e: Exception) {
                    Log.e("CameraX", "Binding failed: ${e.message}", e)
                }
            }
        }

        return Pair(imageCapture, videoCapture)
    }



    fun takePhoto(context: Context, imageCapture: ImageCapture) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            // Save photo to Pictures/AR Drawer
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AR Drawer")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(context, "Photo saved to AR Drawer!", Toast.LENGTH_SHORT).show()
                    Log.d("CameraX", "Photo saved to: ${output.savedUri}")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startVideoRecording(
        context: Context,
        videoCapture: VideoCapture<Recorder>,
        onRecordingEvent: (VideoRecordEvent) -> Unit
    ): Recording {
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, "VID_${System.currentTimeMillis()}.mp4")
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/MyApp")
        }

        val outputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        return videoCapture.output
            .prepareRecording(context, outputOptions)
            .withAudioEnabled()
            .start(Executors.newSingleThreadExecutor(), onRecordingEvent)
    }


    fun showSaveDialog(
        onSaveConfirmed: () -> Unit,
        layoutInflater: LayoutInflater,
        context: Context
    ) {
        val dialogView = layoutInflater.inflate(R.layout.custom_save_dailog, null)
        val saveButton = dialogView.findViewById<Button>(R.id.takePhotoBtn)
        val cancelButton = dialogView.findViewById<Button>(R.id.backToHomeBtn)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        saveButton.setOnClickListener {
            onSaveConfirmed()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }




}
