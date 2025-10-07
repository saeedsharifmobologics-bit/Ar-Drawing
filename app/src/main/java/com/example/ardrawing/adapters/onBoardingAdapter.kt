package com.example.ardrawing.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ardrawing.onBoardingScreen.*

class OnboardingPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {


    val fragments =
        mutableListOf(
            0L to LanguageFragment(),
            1L to HowDrawWithCameraFragment(),
            2L to DrawHowWithScreenFragment(),
            3L to HowUYOCFragment(),
            4L to SketchBoxFragment()
        )


    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].second

    // Important: Return unique ID for each fragment
    override fun getItemId(position: Int): Long = fragments[position].first

    // Important: Tell ViewPager2 which IDs still exist
    override fun containsItem(itemId: Long): Boolean {
        return fragments.any { it.first == itemId }
    }

    fun removeFirstPage() {
        if (fragments.isNotEmpty()) {
            fragments.removeAt(0)
            notifyDataSetChanged()
        }
    }
}
