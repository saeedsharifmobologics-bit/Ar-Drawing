package com.sketchbox.drawingapp.statesFragments

import android.net.Uri


data class ImageItem(
    val uri: Uri,
    var isSelected: Boolean = false
)
