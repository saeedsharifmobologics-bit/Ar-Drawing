package com.sketchbox.drawingapp.adsManger


import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

class BillingManager(
    private val context: Context,
    private val productIds: List<String>,
    private val onProductsFetched: (List<ProductDetails>) -> Unit,
    private val onPurchaseCompleted: (Purchase) -> Unit
) {
    private val billingClient = BillingClient.newBuilder(context)
        .setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
        }
        .enablePendingPurchases()
        .build()

    private var productDetailsList: List<ProductDetails> = emptyList()

    fun start() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryAvailableProducts()
                    checkActiveSubscription { hasActive ->
                        Utils.subscriptionState = hasActive
                        Log.d("Billing", "Active sub: $hasActive")
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    private fun queryAvailableProducts() {
        val productList = productIds.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, list ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                this.productDetailsList = list
                onProductsFetched(list)
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
        if (offerToken == null) {
            Log.w("BillingManager", "No offer token for ${productDetails.productId}")
            return
        }

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(params) {
                if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                    onPurchaseCompleted(purchase)
                }
            }
        }
    }

    fun checkActiveSubscription(onResult: (Boolean) -> Unit) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                val active = purchases.any { p -> p.products.any { it in productIds } }
                Utils.subscriptionState = active
                onResult(active)
            } else {
                onResult(false)
            }
        }
    }

    fun getProductDetails(): List<ProductDetails> = productDetailsList
}
