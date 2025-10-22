package com.sketchbox.drawingapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sketchbox.drawingapp.onBoardingScreen.*

class OnboardingPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        HowDrawWithCameraFragment(),
        DrawHowWithScreenFragment(),
        HowUYOCFragment(),

    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
