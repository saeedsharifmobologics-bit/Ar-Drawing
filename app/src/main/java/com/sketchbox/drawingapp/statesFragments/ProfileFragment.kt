package com.sketchbox.drawingapp.statesFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.adsManger.adsUtils
import com.sketchbox.drawingapp.databinding.FragmentProfileBinding
import com.sketchbox.drawingapp.utils.ArDrawingSharePreference
import com.sketchbox.drawingapp.utils.ProfileHelper

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
        ScreenStatusLogs.logScreenView("ProfileFragment", "ProfileFragment")

        if (!Utils.subscriptionState) {
            adsUtils.loadNativeAd(requireView(), requireContext())
        }
        sharePreference = ArDrawingSharePreference(requireContext())
        val drawn_Size = ProfileHelper.getImagesFromArDrawer(requireContext()).size
        val spent_Time = ProfileHelper.formatDuration(sharePreference.getSpentCountTime())
        binding.spentTime.text = spent_Time
        binding.drawnSize.text = drawn_Size.toString()



        binding.viewSketchImagesBtn.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAppImagesFragment()
            findNavController().navigate(action)
        }
        binding.viewSketchVideoBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAppVideoFragment())
        }
    }

}