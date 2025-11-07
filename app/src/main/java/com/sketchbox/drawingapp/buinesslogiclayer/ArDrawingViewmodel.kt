package com.sketchbox.drawingapp.buinesslogiclayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sketchbox.drawingapp.dataClass.ArDrawingData
import com.sketchbox.drawingapp.dataClass.OverlayState
import com.sketchbox.drawingapp.dbUtils.ArDrawingDataDao
import com.sketchbox.drawingapp.utils.ProfileHelper.getImagesFromArDrawer
import com.sketchbox.drawingapp.utils.ProfileHelper.getVideoFromArDrawer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArDrawingViewmodel(private val dao: ArDrawingDataDao) : ViewModel() {


    private val _overlayState = MutableStateFlow(OverlayState())
    val overlayState: StateFlow<OverlayState> = _overlayState

    private val _favoriteList = MutableStateFlow<List<ArDrawingData>>(emptyList())
    val favoriteList: StateFlow<List<ArDrawingData>> = _favoriteList

    private val _imagesList = MutableStateFlow<List<Uri>>(emptyList())
    val appImagesList: StateFlow<List<Uri>> = _imagesList

    private val _videosList = MutableStateFlow<List<Uri>>(emptyList())
    val appVideosList: StateFlow<List<Uri>> = _videosList

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> get() = _currentPosition

    fun setCurrentPosition(pos: Int) {
        _currentPosition.value = pos
    }


    init {
        // Observe database changes on initialization
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            dao.getAllFavorites().collectLatest { list ->
                _favoriteList.value = list
            }
        }
    }

    fun loadImagesFromArDrawer(context: Context) {
        viewModelScope.launch {
            val list = getImagesFromArDrawer(context)
            _imagesList.value = list
        }
    }

    fun loadVideosFromArDrawer(context: Context) {
        viewModelScope.launch {
            val list = getVideoFromArDrawer(context)
            _videosList.value = list
        }
    }


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

    //Add new favorite
    fun addFavorite(url: String) {
        viewModelScope.launch {
            dao.addFavorite(ArDrawingData(favouriteUrl = url))
        }
    }


    fun removeFavorite(url: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val rowDeleted = dao.removeFavorite(url)
            onResult(rowDeleted > 0)
        }
    }


}