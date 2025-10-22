package com.sketchbox.drawingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.databinding.FragmentSelectionModeBinding
import com.sketchbox.drawingapp.utils.ArDrawingSharePreference
import com.sketchbox.drawingapp.utils.PermissionHandler
import kotlinx.coroutines.launch

enum class DrawMode { SCREEN, CAMERA, NONE }

class SelectionModeFragment : Fragment() {
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var sharePreference: ArDrawingSharePreference
    private var pendingAction: (() -> Unit)? = null

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()

                pendingAction?.invoke()
                pendingAction = null // Reset after calling
            } else {
                val requestCount = sharePreference.getCameraPermissionCount()
                when (requestCount) {
                    0 -> {
                        sharePreference.saveCameraPermissionCount(1)
                        permissionHandler.showRetryDialog("CameraPermission")
                    }

                    else -> {
                        permissionHandler.showSettingsDialog()
                    }
                }
            }
        }

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
        ScreenStatusLogs.logScreenView("SelectionModeFragment","SelectionModeFragment")

        updateSelection()
        permissionHandler= PermissionHandler(requireContext(),cameraPermissionLauncher)
        sharePreference= ArDrawingSharePreference(requireContext())
        binding.drawWithScreenSelectionBtn.setOnClickListener {

            selectedMode = DrawMode.SCREEN
            updateSelection()


        }
        binding.drawWithCameraSelectionBtn.setOnClickListener {
            selectedMode = DrawMode.CAMERA
            updateSelection()
        }


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch{

        }

        binding.continueBtn.setOnClickListener {
            when (selectedMode) {
                DrawMode.SCREEN -> {
                    // No permission required, navigate directly
                    val action = SelectionModeFragmentDirections
                        .actionSelectionModeFragmentToCameraPreviewFragment(255)
                    findNavController().navigate(action)
                }

                DrawMode.CAMERA -> {
                    if (permissionHandler.isCameraPermissionGranted()) {
                        val action = SelectionModeFragmentDirections
                            .actionSelectionModeFragmentToCameraPreviewFragment(100)
                        findNavController().navigate(action)
                    } else {
                        // Permission not granted, save action as pending and request permission
                        pendingAction = { val action = SelectionModeFragmentDirections.actionSelectionModeFragmentToCameraPreviewFragment(100)
                            findNavController().navigate(action)
                        }
                        permissionHandler.requestCameraPermission()
                    }
                }

                DrawMode.NONE -> {
                    Toast.makeText(requireContext(), "Please Select Mode", Toast.LENGTH_SHORT).show()
                }
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