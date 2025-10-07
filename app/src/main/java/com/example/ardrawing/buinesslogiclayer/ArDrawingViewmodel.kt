package com.example.ardrawing.buinesslogiclayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.ardrawing.data.OverlayState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ArDrawingViewmodel : ViewModel() {


    var bitmap: Bitmap? = null

    private val _overlayState = MutableStateFlow(OverlayState())
    val overlayState: StateFlow<OverlayState> = _overlayState


    private var cameraProvider: ProcessCameraProvider? = null




    fun getCameraProvider(context: Context, onReady: (ProcessCameraProvider) -> Unit) {
        if (cameraProvider != null) {
            onReady(cameraProvider!!)
            return
        }
        ProcessCameraProvider.getInstance(context).also { future ->
            future.addListener({
                cameraProvider = future.get()
                onReady(cameraProvider!!)
            }, ContextCompat.getMainExecutor(context))
        }
    }


    // A more conventional way to update StateFlow
    private fun updateState(update: (OverlayState) -> OverlayState) {
        _overlayState.update(update)
    }


    fun setSketchLevel(level: Int) {
        _overlayState.update { it.copy(sketchLevel = level) }
    }
    fun resetState(defaultRect: RectF) {
        updateState { currentState ->
            currentState.copy(
                rectF = RectF(defaultRect), // Reset position to the center
                scaleFactor = 1.0f          // Reset scale to default
            )
        }
    }
    // ---------------------------------------------

    fun setAlpha(value: Int) {
        updateState { it.copy(alpha = value) }
    }

    fun setScale(factor: Float) {
        updateState { it.copy(scaleFactor = factor) }
    }

    fun setLocked(locked: Boolean) {
        updateState { it.copy(isLocked = locked) }
    }

    fun setStrokeMask(mask: Bitmap?) {
        updateState { it.copy(strokeMask = mask) }
    }

    fun setPosition(newRect: RectF) {
        updateState { it.copy(rectF = RectF(newRect)) }
    }

    fun setDetectionEnabled(enabled: Boolean) {
        updateState { it.copy(isDetectionEnabled = enabled) }
    }


}