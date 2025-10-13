package com.example.ardrawing.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ardrawing.R
import com.example.ardrawing.databinding.FragmentProfileBinding
import com.example.ardrawing.utils.ArDrawingSharePreference


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
        sharePreference = ArDrawingSharePreference(requireContext())
        binding.spentTime.text = sharePreference.getSpentCountTime().toString()
    }

}