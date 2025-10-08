package com.example.ardrawing.onBoardingScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ardrawing.R
import com.example.ardrawing.adapters.LanguageAdapter
import com.example.ardrawing.databinding.ActivityLanguageBinding
import com.example.ardrawing.databinding.FragmentLanguageBinding
import com.example.ardrawing.utils.AppLaunchPrefs
import com.example.ardrawing.utils.LanguageManager

class LanguageActivity : AppCompatActivity() {
    lateinit var binding: ActivityLanguageBinding
    private var selectedLang: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupLanguageList()

        binding.continueBtn.setOnClickListener {
            if (selectedLang.isEmpty()) {
                Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Store selected language globally
            LanguageManager.selectedLang = selectedLang
            LanguageManager.setLanguage(this, LanguageManager.selectedLang)
            AppLaunchPrefs.setFirstLaunchDone(this,false)
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()

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


}