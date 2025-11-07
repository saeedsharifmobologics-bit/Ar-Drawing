package com.sketchbox.drawingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.ProductDetails
import com.sketchbox.drawingapp.ArApp
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.databinding.FragmentSubscriptionBinding

class SubscriptionFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionBinding
    private lateinit var app: ArApp
    private var selectedProduct: ProductDetails? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        app = requireActivity().application as ArApp
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        ScreenStatusLogs.logScreenView("SubscriptionFragment", "SubscriptionFragment")

        val products = Utils.productsList
        val planButtons = listOf(weeklyPlanBtn, montlyPlanBtn, yearlyPlanBtn)
        val priceTexts = listOf(weeklyPlanePrice, montlyPlanePrice, yearlyPlanePrice)

        // Set prices if products are available
        if (products.isNotEmpty()) {
            products.forEachIndexed { index, product ->
                val price = product.subscriptionOfferDetails
                    ?.firstOrNull()
                    ?.pricingPhases
                    ?.pricingPhaseList
                    ?.firstOrNull()
                    ?.formattedPrice ?: "$0.00"
                priceTexts.getOrNull(index)?.text = price
            }

            // Default weekly plan selected with background
            selectedProduct = products.getOrNull(0)
                updateSelectedCard(weeklyPlanBtn, planButtons)
        } else {
            // Even if no product, show weekly card as selected visually
            updateSelectedCard(weeklyPlanBtn, planButtons)
        }

        // Plan selection clicks
        planButtons.forEachIndexed { index, layout ->
            layout.setOnClickListener {
                selectedProduct = products.getOrNull(index)
                updateSelectedCard(layout, planButtons)
            }
        }

        // Subscribe button click
        subscribeBtn.setOnClickListener {
            selectedProduct?.let { product ->
                app.billingManager.launchPurchaseFlow(requireActivity(), product)
            }
        }

        // Close button
        btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateSelectedCard(selected: LinearLayout, all: List<LinearLayout>) {
        all.forEach { layout ->
            val bg = if (layout == selected) R.drawable.price_selected_card_bg else R.drawable.prices_card_bg
            layout.background = ContextCompat.getDrawable(requireContext(), bg)
        }
    }
}
