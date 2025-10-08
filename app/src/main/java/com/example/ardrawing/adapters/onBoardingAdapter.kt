package com.example.ardrawing.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ardrawing.onBoardingScreen.*

class OnboardingPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        HowDrawWithCameraFragment(),
        DrawHowWithScreenFragment(),
        HowUYOCFragment(),
        SketchBoxFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
