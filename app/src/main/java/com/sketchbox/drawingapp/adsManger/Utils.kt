package com.sketchbox.drawingapp.adsManger

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails

@Keep
object Utils {
    var productsList: List<ProductDetails> = emptyList()
    var subscriptionState: Boolean = false



}
