package com.example.ardrawing.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.ardrawing.R
import com.example.ardrawing.databinding.FragmentSelectionModeBinding

enum class DrawMode { SCREEN, CAMERA, NONE }

class SelectionModeFragment : Fragment() {
    lateinit var binding: FragmentSelectionModeBinding

    companion object {
        public var selectedMode: DrawMode = DrawMode.NONE

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectionModeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSelection()
        binding.drawWithScreenSelectionBtn.setOnClickListener {

            selectedMode = DrawMode.SCREEN
            updateSelection()


        }
        binding.drawWithCameraSelectionBtn.setOnClickListener {
            selectedMode = DrawMode.CAMERA
            updateSelection()
        }


        binding.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }


        binding.continueBtn.setOnClickListener {
            val action = when (selectedMode) {
                DrawMode.SCREEN -> {
                    SelectionModeFragmentDirections.actionSelectionModeFragmentToCameraPreviewFragment(
                        255
                    )
                }

                DrawMode.CAMERA -> {
                    SelectionModeFragmentDirections.actionSelectionModeFragmentToCameraPreviewFragment(
                        100
                    )
                }

                DrawMode.NONE -> {
                    Toast.makeText(requireContext(), "Please Select Mode", Toast.LENGTH_SHORT)
                        .show()
                    null
                }
            }

            // Only navigate if action is not null and Dr is true
            if (action != null) {

                findNavController().navigate(action)
            }


        }

    }

    private fun updateSelection() {
        when (selectedMode) {
            DrawMode.SCREEN -> {
                // Screen selected
                binding.drawWithScreenSelectionBtn.setBackgroundResource(R.drawable.selection_card_border) // selected bg
                binding.imageTick2.visibility = View.VISIBLE

                // Reset Camera
                binding.drawWithCameraSelectionBtn.setBackgroundResource(android.R.color.transparent)
                binding.imageTick.visibility = View.GONE
            }

            DrawMode.CAMERA -> {
                // Camera selected
                binding.drawWithCameraSelectionBtn.setBackgroundResource(R.drawable.selection_card_border) // selected bg
                binding.imageTick.visibility = View.VISIBLE

                // Reset Screen
                binding.drawWithScreenSelectionBtn.setBackgroundResource(android.R.color.transparent)
                binding.imageTick2.visibility = View.GONE
            }

            DrawMode.NONE -> {
                // Reset both
                binding.drawWithCameraSelectionBtn.setBackgroundResource(android.R.color.transparent)
                binding.imageTick.visibility = View.GONE

                binding.drawWithScreenSelectionBtn.setBackgroundResource(android.R.color.transparent)
                binding.imageTick2.visibility = View.GONE
            }
        }
    }



}