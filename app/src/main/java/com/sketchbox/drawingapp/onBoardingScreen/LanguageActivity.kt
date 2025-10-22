package com.sketchbox.drawingapp.onBoardingScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adapters.LanguageAdapter
import com.sketchbox.drawingapp.databinding.ActivityLanguageBinding
import com.sketchbox.drawingapp.utils.ArDrawingSharePreference
import com.sketchbox.drawingapp.utils.LanguageManager
import com.sketchbox.drawingapp.adsManger.adsUtils
import com.sketchbox.drawingapp.utils.LanguageManager.isLanguageChanging

class LanguageActivity : AppCompatActivity() {
    lateinit var binding: ActivityLanguageBinding
    private var selectedLang: String = "en"
    lateinit var arDrawingSharePreference: ArDrawingSharePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        arDrawingSharePreference = ArDrawingSharePreference(this)
        setupLanguageList()

        binding.continueBtn.setOnClickListener {
            if (selectedLang.isEmpty()) {
                Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()

            } else {
                isLanguageChanging = true
                // Store selected language globally
                LanguageManager.selectedLang = selectedLang
                LanguageManager.setLanguage(this, LanguageManager.selectedLang)
                arDrawingSharePreference.setFirstLaunchDone(this, false)
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }


        }


    }

    private fun setupLanguageList() {
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

        val adapter = LanguageAdapter(languages) { langCode ->
            selectedLang = langCode
        }

        binding.languageRv.layoutManager = LinearLayoutManager(this)
        binding.languageRv.adapter = adapter

    }


    override fun onResume() {
        super.onResume()
        adsUtils.loadBannerAd(binding.root, this)

    }

}