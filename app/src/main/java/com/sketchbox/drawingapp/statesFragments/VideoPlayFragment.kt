package com.sketchbox.drawingapp.statesFragments

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.databinding.FragmentVideoPlayBinding

class VideoPlayFragment : Fragment() {

    private lateinit var binding: FragmentVideoPlayBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentVideoPlayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: VideoPlayFragmentArgs by navArgs()
        val videoUri = args.videoUri.toUri()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.videoView.setVideoURI(videoUri)

        // ---- VIDEO READY ----
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            binding.videoView.start()
            binding.videoSeekbar.max = mediaPlayer.duration
            binding.txtTotalTime.text = formatTime(mediaPlayer.duration)

            handler.post(object : Runnable {
                override fun run() {
                    if (binding.videoView.isPlaying) {
                        val currentPos = binding.videoView.currentPosition
                        binding.videoSeekbar.progress = currentPos
                        binding.btnPlayPause.setImageResource(R.drawable.video_pause_button) // play icon

                        binding.txtCurrentTime.text = formatTime(currentPos)
                    }
                    handler.postDelayed(this, 500)
                }
            })
        }

        binding.videoView.setOnCompletionListener {
            binding.btnPlayPause.setImageResource(R.drawable.video_start_btn_ic) // play icon
            binding.videoSeekbar.progress = 0
            binding.txtCurrentTime.text = "00:00"
        }

        // ---- SEEK VIDEO MANUALLY ----
        binding.videoSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) binding.videoView.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.btnPlayPause.setOnClickListener {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                binding.btnPlayPause.setImageResource(R.drawable.video_start_btn_ic)
            } else {
                binding.videoView.start()
                binding.btnPlayPause.setImageResource(R.drawable.video_pause_button)
            }
        }


        binding.deleteBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().getString(R.string.delete_video))
                .setMessage(requireContext().getString(R.string.delete_video_dec))
                .setPositiveButton(requireContext().getString(R.string.delete)) { _, _ ->
                        val uri = args.videoUri.toUri()
                        val deleted = deleteVideoByUri(requireContext(), uri)

                        if (deleted) {
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
                        }


                }
                .setNegativeButton(requireContext().getString(R.string.no_dialog), null)
                .show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = milliseconds / 1000 % 60
        val minutes = milliseconds / 1000 / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    fun deleteVideoByUri(context: Context, videoUri: Uri): Boolean {
        return try {
            val rowsDeleted = context.contentResolver.delete(videoUri, null, null)
            rowsDeleted > 0
            findNavController().popBackStack()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
