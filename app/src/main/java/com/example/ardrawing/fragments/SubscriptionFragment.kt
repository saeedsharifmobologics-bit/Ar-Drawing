package com.example.ardrawing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.billingclient.api.ProductDetails
import com.example.ardrawing.ArApp
import com.example.ardrawing.R
import com.example.ardrawing.adsManger.ScreenStatusLogs
import com.example.ardrawing.adsManger.Utils
import com.example.ardrawing.databinding.FragmentSubscriptionBinding

class SubscriptionFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionBinding
    private var selectedProduct: ProductDetails? = null
    private lateinit var app: ArApp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        app = requireActivity().application as ArApp
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        ScreenStatusLogs.logScreenView("SubscriptionFragment","SubscriptionFragment")

        //Fetch available products
        val products = Utils.productsList
        if (products.isNotEmpty()) {
            selectedProduct = products[0]
            weeklyPlanePrice.text = products.getOrNull(0)?.subscriptionOfferDetails?.firstOrNull()
                ?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: "$0.00"
            montlyPlanePrice.text = products.getOrNull(1)?.subscriptionOfferDetails?.firstOrNull()
                ?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: "$0.00"
            yearlyPlanePrice.text = products.getOrNull(2)?.subscriptionOfferDetails?.firstOrNull()
                ?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: "$0.00"
        }

        //Plan selection
        val planButtons = listOf(weeklyPlanBtn, montlyPlanBtn, yearlyPlanBtn)
        planButtons.forEachIndexed { index, layout ->
            layout.setOnClickListener {
                updatePriceCard(layout, planButtons)
                selectedProduct = products.getOrNull(index)
            }
        }

        // 🔹 Subscribe button
        subscribeBtn.setOnClickListener {
            selectedProduct?.let { product ->
                app.billingManager.launchPurchaseFlow(requireActivity(), product)
            }
        }

        // 🔹 Close button
        btnClose.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun updatePriceCard(selectedLayout: LinearLayout, allLayouts: List<LinearLayout>) {
        allLayouts.forEach { layout ->
            val bg = if (layout == selectedLayout)
                R.drawable.price_selected_card_bg
            else
                R.drawable.prices_card_bg
            layout.background = ContextCompat.getDrawable(requireContext(), bg)
        }
    }
}
