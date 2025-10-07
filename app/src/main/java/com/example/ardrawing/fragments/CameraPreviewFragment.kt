package com.example.ardrawing.fragments

import CameraPreviewUtils.showSaveDialog
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.AppCompatButton
import androidx.camera.core.*
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ardrawing.databinding.FragmentCameraPreviewBinding
import com.example.ardrawing.utils.ImageHolder
import com.example.ardrawing.R
import com.example.ardrawing.buinesslogiclayer.ArDrawingViewmodel
import com.example.ardrawing.fragments.SelectionModeFragment.Companion.selectedMode
import com.example.ardrawing.utils.CommonUtils
import com.example.ardrawing.utils.CommonUtils.updateButtonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.opencv.android.Utils
import org.opencv.core.Mat

@TransformExperimental
class CameraPreviewFragment : Fragment() {

    // 1. Enum to track recording state
    enum class RecordingState {
        IDLE,
        RECORDING,
        PAUSED
    }

    // 2. Variables and ViewModel
    private var recordingState: RecordingState = RecordingState.IDLE
    private lateinit var binding: FragmentCameraPreviewBinding
    private lateinit var preview: PreviewView
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    lateinit var bitmap: Bitmap
    private var timerHandler: Handler? = null
    private var timerRunnable: Runnable? = null
    private var elapsedTimeMillis: Long = 0L // total elapsed time
    private var lastStartTime: Long = 0L // when recording/resumed last
    private var opacitySeekbarValue: Int? = 20
    private var sketchIntensity: Float? = 0.2f

    private val viewModel: ArDrawingViewmodel by viewModel()

    // 3. Lifecycle Methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (selectedMode == DrawMode.CAMERA) {
            opacitySeekbarValue=150
            sketchIntensity=0.4f

        } else {
            binding.cameraPreveiw.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.videoStartBtn.visibility = View.GONE
            binding.videoTimeStamp.visibility = View.GONE
            binding.saveBtn.visibility = View.GONE
            opacitySeekbarValue=255
        }
        setupNavigation()
        setupCustomView()

        setupSeekbars()
        if (selectedMode== DrawMode.CAMERA){
            setupCamera()

        }

        setupButtons()
    }

    // --- Setup functions broken into logical chunks ---

    private fun setupNavigation() {


        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
            ImageHolder.pickLocation = null
        }

        // Set transparency in ViewModel & seekbar
        viewModel.setAlpha(opacitySeekbarValue!!)
        binding.seekbar.progress = opacitySeekbarValue!!
    }

    private fun setupCustomView() {
        // Ensure ViewModel is set first
        binding.customView.setViewModel(viewModel, requireActivity())
        bitmap = ImageHolder.bitmap!!
        binding.customView.image = bitmap

        val imagePickLocation = ImageHolder.pickLocation

        // Convert bitmap to Mat and store in CommonUtils
        val tempMat = Mat()
        Utils.bitmapToMat(bitmap, tempMat)
        CommonUtils.srcMat = tempMat

        // Brightness Controls visible only for gallery/camera images
        if (imagePickLocation == "gallery" || imagePickLocation == "camera") {
            binding.screenBrightness.visibility = View.VISIBLE
            binding.sketchSeekbar.visibility = View.VISIBLE

            var sketchJob: Job? = null

            lifecycleScope.launch {
                val sketchBitmap = withContext(Dispatchers.Default) {
                    CommonUtils.applySketchOverlayInBackground(sketchIntensity!!)
                }
                binding.customView.updateSketchImage(sketchBitmap)
            }

            binding.sketchSeekbar.progress = (sketchIntensity!! * 100).toInt()
            binding.sketchSeekbar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    sketchJob?.cancel()
                    sketchJob = lifecycleScope.launch {
                        delay(120)
                        val sketchBitmap = withContext(Dispatchers.Default) {
                            CommonUtils.applySketchOverlayInBackground(progress / 100f)
                        }
                        binding.customView.updateSketchImage(sketchBitmap)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        } else {
            binding.screenBrightness.visibility = View.GONE
            binding.sketchSeekbar.visibility = View.GONE
        }
    }

    private fun setupSeekbars() {
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.setAlpha(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    @SuppressLint("MissingPermission")
    private fun setupCamera() {
        preview = binding.cameraPreveiw

        val (capturedImageCapture, capturedVideoCapture) = CameraPreviewUtils.startCamera(
            this, preview, viewModel, viewLifecycleOwner
        )
        imageCapture = capturedImageCapture
        videoCapture = capturedVideoCapture
    }

    private fun setupButtons() {
        updateRecordingBtn()

        // Info Dialog button
        binding.infoBtn.setOnClickListener {
            showSelectionDialog()
        }

        // Video Start / Stop button
        binding.videoStartBtn.setOnClickListener {
            when (recordingState) {
                RecordingState.IDLE -> startRecording()
                RecordingState.RECORDING, RecordingState.PAUSED -> showPauseResumeDialog()
            }
        }

        // Long press opens Pause/Resume dialog
        binding.videoStartBtn.setOnLongClickListener {
            if (recordingState == RecordingState.RECORDING || recordingState == RecordingState.PAUSED) {
                showPauseResumeDialog()
                true
            } else false
        }

        // Rotate and Capture buttons
        binding.rotateLeft.setOnClickListener { binding.customView.rotate90ToLeft() }

        binding.rotateRight.setOnClickListener { binding.customView.rotate90ToRight() }

        binding.saveBtn.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                showSaveDialog({
                    imageCapture?.let { capture ->
                        CameraPreviewUtils.takePhoto(requireContext(), capture)
                    }
                }, layoutInflater, requireContext())
            }, 300)
        }
    }

    // --- Recording Control Functions ---

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun startRecording() {
        recording = CameraPreviewUtils.startVideoRecording(
            requireContext(),
            videoCapture!!
        ) { event ->
            when (event) {
                is VideoRecordEvent.Start -> {
                    Handler(Looper.getMainLooper()).post {
                        recordingState = RecordingState.RECORDING
                        updateRecordingBtn()
                        startRecordingTimer()

                        Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()
                    }
                }

                is VideoRecordEvent.Finalize -> {
                    Handler(Looper.getMainLooper()).post {
                        recordingState = RecordingState.IDLE
                        updateRecordingBtn()
                        stopRecordingTimer()

                        Toast.makeText(context, "Recording Saved", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startRecordingTimer() {
        lastStartTime = System.currentTimeMillis()

        if (timerHandler == null) {
            timerHandler = Handler(Looper.getMainLooper())
        }

        timerRunnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                val totalTime = elapsedTimeMillis + (currentTime - lastStartTime)
                val seconds = totalTime / 1000f
                binding.videoTimeStamp.text = String.format("%.1f Sec", seconds)

                timerHandler?.postDelayed(this, 100)
            }
        }

        timerHandler?.post(timerRunnable!!)
    }

    private fun pauseRecordingTimer() {
        val now = System.currentTimeMillis()
        elapsedTimeMillis += (now - lastStartTime)
        timerHandler?.removeCallbacks(timerRunnable!!)
    }

    private fun stopRecordingTimer() {
        timerHandler?.removeCallbacks(timerRunnable!!)
        timerHandler = null
        timerRunnable = null
        elapsedTimeMillis = 0L
        lastStartTime = 0L
        binding.videoTimeStamp.text = "0.0 Sec"
    }

    private fun updateRecordingBtn() {
        when (recordingState) {
            RecordingState.IDLE -> {
                binding.videoStartBtn.setCompoundDrawablesWithIntrinsicBounds(
                    0, R.drawable.video_start_btn_ic, 0, 0
                )
            }

            RecordingState.RECORDING -> {
                binding.videoStartBtn.setCompoundDrawablesWithIntrinsicBounds(
                    0, R.drawable.video_stop_btn_ic, 0, 0
                )
            }

            RecordingState.PAUSED -> {
                binding.videoStartBtn.setCompoundDrawablesWithIntrinsicBounds(
                    0, R.drawable.video_pause_button, 0, 0
                )
            }
        }
    }

    private fun showPauseResumeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_recording_dialog, null)
        val pauseResumeBtn = dialogView.findViewById<AppCompatButton>(R.id.btnPauseRecording)
        val stopBtn = dialogView.findViewById<AppCompatButton>(R.id.btnStopRecording)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set pause/resume text dynamically
        pauseResumeBtn.text = when (recordingState) {
            RecordingState.PAUSED -> "Resume Recording"
            RecordingState.RECORDING -> "Pause Recording"
            else -> "Pause Recording"
        }

        pauseResumeBtn.setOnClickListener {
            when (recordingState) {
                RecordingState.RECORDING -> {
                    recording?.pause()
                    pauseRecordingTimer()
                    recordingState = RecordingState.PAUSED
                    updateRecordingBtn()
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Recording Paused", Toast.LENGTH_SHORT).show()
                }

                RecordingState.PAUSED -> {
                    recording?.resume()
                    recordingState = RecordingState.RECORDING
                    updateRecordingBtn()
                    startRecordingTimer()
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Recording Resumed", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Recording not active", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
            }
        }

        stopBtn.setOnClickListener {
            try {
                recording?.stop()
            } catch (e: Exception) {
                Log.e("CameraX", "Error stopping recording: ${e.message}", e)
            } finally {
                recordingState = RecordingState.IDLE
                updateRecordingBtn()
                dialog.dismiss()
                Toast.makeText(requireContext(), "Recording Stopped", Toast.LENGTH_SHORT).show()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({ dialog.show() }, 200)
    }

    // --- Other dialogs ---

    private fun showSelectionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.info_dailog, null)
        val cameraBtn = dialogView.findViewById<AppCompatButton>(R.id.drawWithCameraBtn)
        val screenBtn = dialogView.findViewById<AppCompatButton>(R.id.drawWithScreenBtn)
        val gifImage = dialogView.findViewById<ImageView>(R.id.gif_image_vieww)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        updateButtonState(cameraBtn, screenBtn, true, requireContext(), gifImage)

        cameraBtn.setOnClickListener {
            updateButtonState(cameraBtn, screenBtn, true, requireContext(), gifImage)
        }

        screenBtn.setOnClickListener {
            updateButtonState(cameraBtn, screenBtn, false, requireContext(), gifImage)
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        ImageHolder.pickLocation = null
    }
}
