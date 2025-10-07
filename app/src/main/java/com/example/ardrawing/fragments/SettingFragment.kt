package com.example.ardrawing.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ardrawing.MainActivity
import com.example.ardrawing.R
import com.example.ardrawing.databinding.FragmentSettingBinding
import com.example.ardrawing.utils.CommonUtils


class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = (requireActivity() as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        binding.backBtn.setOnClickListener {
            (activity as? MainActivity)?.closeDrawer()
        }

        binding.shareAppBtn.setOnClickListener {
            CommonUtils.shareApp(requireContext())
        }

        binding.moreAppsBtn.setOnClickListener {
            CommonUtils.openUrl(
                requireContext(),
                "https://play.google.com/store/apps/developer?id=Energy+apps"
            )
        }

        binding.goPremiumLayout.setOnClickListener {

            (activity as? MainActivity)?.closeDrawer()
            navController?.navigate(R.id.subscriptionFragment)
        }

        binding.library.setOnClickListener {
            (activity as? MainActivity)?.closeDrawer()
            navController?.navigate(R.id.appLibraryFragment)
        }


        binding.privacyPolicyBtn.setOnClickListener {
            CommonUtils.openUrl(
                requireContext(),
                "https://chatgpt.com/?openaicom_referred=true"
            )
        }
        binding.termsOfServiceBtn.setOnClickListener {
            CommonUtils.openUrl(
                requireContext(),
                "https://policies.google.com/terms?hl=en"
            )
        }
        binding.home.setOnClickListener { (activity as? MainActivity)?.closeDrawer() }

    }


}