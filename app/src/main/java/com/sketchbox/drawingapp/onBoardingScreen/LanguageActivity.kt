package com.sketchbox.drawingapp.onBoardingScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adapters.LanguageAdapter
import com.sketchbox.drawingapp.adsManger.adsUtils
import com.sketchbox.drawingapp.databinding.ActivityLanguageBinding
import com.sketchbox.drawingapp.utils.ArDrawingSharePreference
import com.sketchbox.drawingapp.utils.LanguageManager
import com.sketchbox.drawingapp.utils.LanguageManager.isLanguageChanging

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var arDrawingSharePreference: ArDrawingSharePreference
    private var selectedLang: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        arDrawingSharePreference = ArDrawingSharePreference(this)

        selectedLang = LanguageManager.getLanguage(this)
        Log.d("LanguageActivity", "Saved language code: $selectedLang")

        setupLanguageList(selectedLang)

        // Continue button click
        binding.continueBtn.setOnClickListener {
            if (selectedLang.isEmpty()) {
                Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()
            } else {
                isLanguageChanging = true
                LanguageManager.selectedLang = selectedLang
                LanguageManager.setLanguage(this, selectedLang)
                arDrawingSharePreference.setFirstLaunchDone(this, false)

                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }
    }

    // ✅ Updated setupLanguageList function to accept currentLang
    private fun setupLanguageList(currentLang: String) {
        val languages = listOf(
            "English" to "en",
            "Spanish" to "es",
            "French" to "fr",
            "German" to "de",
            "Chinese" to "zh",
            "Japanese" to "ja",
            "Korean" to "ko",
            "Arabic" to "ar",
            "Russian" to "ru",
            "Portuguese" to "pt",
            "Urdu" to "ur"
        )

        Log.d("LanguageActivity", "Setting up adapter with selected language: $currentLang")

        val adapter = LanguageAdapter(languages, currentLang) { langCode ->
            selectedLang = langCode
            Log.d("LanguageActivity", "User selected: $langCode")
        }

        binding.languageRv.layoutManager = LinearLayoutManager(this)
        binding.languageRv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        /*
        adsUtils.loadBannerAd(binding.root, this)
        */
    }
}
