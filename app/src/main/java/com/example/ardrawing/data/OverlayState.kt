package com.example.ardrawing.data

import android.graphics.Bitmap
import android.graphics.RectF

data class OverlayState(
    val alpha: Int = 20,
    val scaleFactor: Float = 1f,
    val isLocked: Boolean = false,
    val isDetectionEnabled: Boolean = false,
    val rectF: RectF = RectF(),
    val strokeMask: Bitmap? = null,
    val sketchLevel: Int = 0 // 👈 naya property
)