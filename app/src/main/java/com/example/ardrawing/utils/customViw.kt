package com.example.ardrawing.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withRotation
import androidx.core.graphics.withScale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ardrawing.R
import com.example.ardrawing.buinesslogiclayer.ArDrawingViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.apply
import kotlin.let
import kotlin.math.atan2
import kotlin.ranges.coerceIn

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private var rotationDegrees = 0f // Rotation from default (0°)
    private var initialRotation = 0f
    private var isRotating = false

    // --- Constants ---
    private companion object {
        const val CIRCLE_RADIUS = 40f
        const val MIN_SCALE = 0.5f
        const val MAX_SCALE = 3.0f
        const val RECT_CORNER_RADIUS = 30f
        const val BORDER_STROKE_WIDTH = 14f
        const val STROKE_MASK_ALPHA = 120
    }

    // --- Properties ---
    private var viewModel: ArDrawingViewmodel? = null
    var image: Bitmap? = null
        set(value) {
            field = value
            invalidate() // Redraw the view when a new image is set
        }

    // This property will be set asynchronously
    private var lockIcon: Bitmap? = null

    // State variables
    private var rectF = RectF()
    private val defaultRectF = RectF() // Holds the initial position and size
    private var scaleFactor = 1f
    private var alphaValue = 255
    private var isLocked = false
    private var isDetectionEnabled = false
    private var strokeMask: Bitmap? = null

    // Touch handling state
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isMoving = false
    private var isScaling = false

    // UI elements
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lockCircleRect = RectF()
    private var resetCircleRect = RectF()

    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())

    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                resetToDefaultState()
                invalidate()
                return true
            }
        })

    // --- ViewModel Setup ---
    fun setViewModel(vm: ArDrawingViewmodel, lifecycleOwner: LifecycleOwner) {
        viewModel = vm
        lifecycleOwner.lifecycleScope.launch {
            // Load the lock icon in a coroutine
            lockIcon = getBitmapFromVectorDrawable(context, R.drawable.baseline_lock_24)

            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Collect state from ViewModel and update local properties
                vm.overlayState.collect { state ->
                    alphaValue = state.alpha
                    scaleFactor = state.scaleFactor
                    isLocked = state.isLocked
                    strokeMask = state.strokeMask
                    rectF.set(state.rectF)
                    isDetectionEnabled = state.isDetectionEnabled
                    updateControlPositions()
                    invalidate()
                }
            }
        }
    }

    private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.toDegrees(atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())).toFloat()
    }
    // --- View Lifecycle & Sizing ---
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupDefaultRectangle(w, h)
        viewModel?.setPosition(RectF(rectF))
    }

    private fun setupDefaultRectangle(w: Int, h: Int) {
        val margin = 0.05f
        val availableWidth = w * (1 - margin * 2)
        val availableHeight = h * (1 - margin * 2)
        val targetAspect = 4f / 3f

        var rectWidth = availableWidth
        var rectHeight = rectWidth / targetAspect

        if (rectHeight > availableHeight) {
            rectHeight = availableHeight
            rectWidth = rectHeight * targetAspect
        }

        val left = (w - rectWidth) / 2
        val top = (h - rectHeight) / 2

        defaultRectF.set(left, top, left + rectWidth, top + rectHeight)
        rectF.set(defaultRectF)
        updateControlPositions()
    }

    private fun updateControlPositions() {
        val diameter = CIRCLE_RADIUS * 2
        lockCircleRect.set(rectF.right - diameter, rectF.top, rectF.right, rectF.top + diameter)
        resetCircleRect.set(rectF.right - diameter, rectF.bottom - diameter, rectF.right, rectF.bottom)
    }

    // --- Drawing ---
    override fun onDraw(canvas: Canvas) {
        canvas.withScale(scaleFactor, scaleFactor, rectF.centerX(), rectF.centerY()) {
            withRotation(rotationDegrees, rectF.centerX(), rectF.centerY()) {
                drawMainRectangle(this)
                drawStrokeMask(this)
                drawBorder(this)
                drawLockButton(this)
                drawResetButton(this)
            }
        }
    }



    private fun drawMainRectangle(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            alpha = alphaValue
        }

        val path = Path().apply {
            addRoundRect(
                rectF,
                RECT_CORNER_RADIUS,
                RECT_CORNER_RADIUS,
                Path.Direction.CW
            )
        }
        canvas.clipPath(path)
        canvas.drawBitmap(image!!, null, rectF, paint)
    }

    private fun drawStrokeMask(canvas: Canvas) {
            strokeMask?.let {
                val overlayPaint = Paint().apply { alpha = STROKE_MASK_ALPHA }
                canvas.drawBitmap(it, null, rectF, overlayPaint)

        }
    }
    fun rotate90ToRight() {
        rotationDegrees = (rotationDegrees + 90f) % 360f
        invalidate()
    }
    fun rotate90ToLeft() {
        rotationDegrees = (rotationDegrees - 90f) % 360f
        invalidate()
    }

    private fun drawBorder(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.STROKE
            color = if (isLocked) Color.GRAY else  ContextCompat.getColor(context, R.color.lines_color)
            strokeWidth = BORDER_STROKE_WIDTH
            alpha = 255
        }
        canvas.drawRoundRect(rectF, RECT_CORNER_RADIUS, RECT_CORNER_RADIUS, paint)
    }

    private fun drawLockButton(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = if (isLocked) Color.DKGRAY else  ContextCompat.getColor(context, R.color.lines_color)
        canvas.drawCircle(lockCircleRect.centerX(), lockCircleRect.centerY(), CIRCLE_RADIUS, paint)

        lockIcon?.let {
            val iconSize = CIRCLE_RADIUS * 1.2f
            val iconRect = RectF(
                lockCircleRect.centerX() - iconSize / 2, lockCircleRect.centerY() - iconSize / 2,
                lockCircleRect.centerX() + iconSize / 2, lockCircleRect.centerY() + iconSize / 2
            )
            canvas.drawBitmap(it, null, iconRect, paint)
        }
    }

    private fun drawResetButton(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color =  ContextCompat.getColor(context, R.color.lines_color)
        canvas.drawCircle(
            resetCircleRect.centerX(),
            resetCircleRect.centerY(),
            CIRCLE_RADIUS,
            paint
        )

        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 30f
        canvas.drawText("↺", resetCircleRect.centerX(), resetCircleRect.centerY() + 10f, paint)
    }

    // --- Touch Handling ---
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        // Adjust touch coordinates for the current scale
        val (x, y) = getTransformedPoint(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> handleActionDown(x, y)
            MotionEvent.ACTION_POINTER_DOWN -> {
                isMoving = false
                if (event.pointerCount == 2) {
                    isRotating = true
                    initialRotation = getAngle(
                        event.getX(0), event.getY(0),
                        event.getX(1), event.getY(1)
                    )
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isRotating && event.pointerCount == 2) {
                    val currentAngle = getAngle(
                        event.getX(0), event.getY(0),
                        event.getX(1), event.getY(1)
                    )
                    val deltaAngle = currentAngle - initialRotation
                    rotationDegrees = (rotationDegrees + deltaAngle) % 360
                    initialRotation = currentAngle
                    invalidate()
                } else {
                    handleActionMove(x, y)
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount <= 2) {
                    isRotating = false
                }
                handlePointerUp(event)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isMoving = false
                isScaling = false
                isRotating = false
            }
        }

        return true
    }

    private fun getTransformedPoint(event: MotionEvent, pointerIndex: Int = 0): PointF {
        val cx = rectF.centerX()
        val cy = rectF.centerY()
        val x = (event.getX(pointerIndex) - cx) / scaleFactor + cx
        val y = (event.getY(pointerIndex) - cy) / scaleFactor + cy
        return PointF(x, y)
    }

    private fun handleActionDown(x: Float, y: Float) {
        when {
            isOnLockCircle(x, y) -> viewModel?.setLocked(!isLocked)
            isOnResetCircle(x, y) -> resetToDefaultState()
            !isLocked && rectF.contains(x, y) -> {
                isMoving = true
                lastTouchX = x
                lastTouchY = y
            }
        }
    }

    private fun handleActionMove(x: Float, y: Float) {
        if (isMoving && !isLocked && !isScaling) {
            val dx = x - lastTouchX
            val dy = y - lastTouchY
            rectF.offset(dx, dy)
            lastTouchX = x
            lastTouchY = y
            updateControlPositions()
            viewModel?.setPosition(RectF(rectF))
            invalidate()
        }
    }

    private fun handlePointerUp(event: MotionEvent) {
        if (event.pointerCount == 2) {
            val remainingIndex = if (event.actionIndex == 0) 1 else 0
            val (x, y) = getTransformedPoint(event, remainingIndex)
            lastTouchX = x
            lastTouchY = y
            isMoving = true
        }
    }

    /** Resets the rectangle's position, size, and scale to their initial default values. */
    private fun resetToDefaultState() {
        rotationDegrees=0f
        viewModel?.resetState(defaultRectF)
    }
    fun updateSketchImage(newBitmap: Bitmap) {
        image = newBitmap
        invalidate()
    }
    private fun isOnLockCircle(x: Float, y: Float) = isPointInCircle(x, y, lockCircleRect)
    private fun isOnResetCircle(x: Float, y: Float) = isPointInCircle(x, y, resetCircleRect)
    private fun isPointInCircle(px: Float, py: Float, circleRect: RectF): Boolean {
        val dx = px - circleRect.centerX()
        val dy = py - circleRect.centerY()
        return (dx * dx + dy * dy) <= (CIRCLE_RADIUS * CIRCLE_RADIUS)
    }

    // --- Scale Gesture Listener ---
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            isScaling = true
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val newScale = (scaleFactor * detector.scaleFactor).coerceIn(MIN_SCALE, MAX_SCALE)
            viewModel?.setScale(newScale)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            isScaling = false
        }
    }
}

// --- Helper Function ---
private suspend fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? =
    withContext(Dispatchers.IO) {
        ContextCompat.getDrawable(context, drawableId)?.let { drawable ->
            createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                .apply {
                    val canvas = Canvas(this)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                }
        }
    }