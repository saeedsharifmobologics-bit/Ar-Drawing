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
import com.example.ardrawing.buinesslogiclayer.OnboardingNavigationListener
import com.example.ardrawing.databinding.ActivityOnBoardingBinding
import com.example.ardrawing.utils.AppLaunchPrefs
import com.example.ardrawing.utils.LanguageManager

class OnboardingActivity : AppCompatActivity(), OnboardingNavigationListener {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var adapter: OnboardingPagerAdapter
    private var hasRemovedFirstPage = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Restore flag from savedInstanceState
        hasRemovedFirstPage = savedInstanceState?.getBoolean("hasRemovedFirstPage") ?: false

        adapter = OnboardingPagerAdapter(this)

        // ✅ Use launchMode instead of hasRemovedFirstPage for initial logic
        if (!AppLaunchPrefs.isFirstLaunch(this)) {
            adapter.removeFirstPage()
            hasRemovedFirstPage = true
        } else if (hasRemovedFirstPage) {
            adapter.removeFirstPage()
        }

        binding.viewPager.adapter = adapter

        setupViewPagerCallbacks()
        setupButtonListeners()

        updateButtonVisibility(binding.viewPager.currentItem)
    }

    private fun setupViewPagerCallbacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonVisibility(position)
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

    private fun updateButtonVisibility(position: Int) {
        if (position == 0 && !hasRemovedFirstPage) {
            binding.continueBtn.visibility = View.GONE
            binding.skipBtn.visibility = View.GONE
        } else {
            when (position) {
                0, 1 -> {
                    binding.continueBtn.text = ContextCompat.getString(this, R.string.next)
                }

                2, 3 -> {
                    binding.continueBtn.text = ContextCompat.getString(this, R.string.lets_start)

                }

            }
            binding.continueBtn.visibility = View.VISIBLE
            binding.skipBtn.visibility = View.VISIBLE
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onLanguageSelectedAndContinue() {
        LanguageManager.setLanguage(this, LanguageManager.selectedLang)
        hasRemovedFirstPage = true
        AppLaunchPrefs.setFirstLaunchDone(this,false)
        recreate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("hasRemovedFirstPage", hasRemovedFirstPage)
    }
}
