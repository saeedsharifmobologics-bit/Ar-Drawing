package com.example.ardrawing.onBoardingScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.ardrawing.MainActivity
import com.example.ardrawing.R
import com.example.ardrawing.adapters.OnboardingPagerAdapter
import com.example.ardrawing.databinding.ActivityOnBoardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var adapter: OnboardingPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = OnboardingPagerAdapter(this)
        binding.viewPager.adapter = adapter

        setupViewPagerCallbacks()
        setupButtonListeners()
        updateButtonState(binding.viewPager.currentItem)
    }

    private fun setupViewPagerCallbacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonState(position)
            }
        })
    }

    private fun setupButtonListeners() {
        binding.continueBtn.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            if (nextItem < adapter.itemCount) {
                binding.viewPager.currentItem = nextItem
            } else {
                navigateToMain()
            }
        }

        binding.skipBtn.setOnClickListener {
            navigateToMain()
        }
    }

    private fun updateButtonState(position: Int) {
        when (position) {
            0, 1 -> {
                binding.continueBtn.text = getString(R.string.next)
            }
            2, 3 -> {
                binding.continueBtn.text = getString(R.string.lets_start)
            }
        }

        binding.continueBtn.visibility = View.VISIBLE
        binding.skipBtn.visibility = View.VISIBLE
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
