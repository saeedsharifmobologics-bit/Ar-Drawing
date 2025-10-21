package com.example.ardrawing.statesFragments

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ardrawing.adsManger.ScreenStatusLogs
import com.example.ardrawing.adsManger.adsUtils
import com.example.ardrawing.databinding.FragmentProfileBinding
import com.example.ardrawing.utils.ArDrawingSharePreference
import com.example.ardrawing.utils.ProfileHelper

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var sharePreference: ArDrawingSharePreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ScreenStatusLogs.logScreenView("ProfileFragment","ProfileFragment")

        adsUtils.loadNativeAd(requireView(),requireContext())
        sharePreference = ArDrawingSharePreference(requireContext())
        val drawn_size=ProfileHelper.getImagesFromArDrawer(requireContext()).size
        val spent_time= ProfileHelper.formatDuration(sharePreference.getSpentCountTime())
        binding.spentTime.text = spent_time
        binding.drawnSize.text=drawn_size.toString()



        binding.viewSketchImagesBtn.setOnClickListener {
            val action= ProfileFragmentDirections.actionProfileFragmentToAppImagesFragment()
            findNavController().navigate(action)
        }
        binding.viewSketchVideoBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAppVideoFragment())
        }
    }

}