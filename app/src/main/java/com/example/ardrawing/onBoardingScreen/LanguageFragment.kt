/*
// com.example.ardrawing.howtouse.LanguageFragment.kt
package com.example.ardrawing.onBoardingScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ardrawing.adapters.LanguageAdapter
import com.example.ardrawing.databinding.FragmentLanguageBinding
import com.example.ardrawing.utils.LanguageManager

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    private var selectedLang: String = "en"
    private var navigationListener: OnboardingNavigationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnboardingNavigationListener) {
            navigationListener = context
        } else {
            throw IllegalStateException("Host activity must implement OnboardingNavigationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLanguageList()
        setupContinueButton()
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
            binding.continueBtn.visibility = View.VISIBLE
        }

        binding.languageRv.layoutManager = LinearLayoutManager(requireContext())
        binding.languageRv.adapter = adapter

    }

    private fun setupContinueButton() {
        binding.continueBtn.setOnClickListener {
            if (selectedLang.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a language", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Store selected language globally
            LanguageManager.selectedLang = selectedLang

            // Trigger onboarding flow to continue
            navigationListener?.onLanguageSelectedAndContinue()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }
}
*/
