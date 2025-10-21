package com.example.ardrawing.adapters.profileAdapter

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.example.ardrawing.R
import com.example.ardrawing.databinding.FragmentVideoPlayBinding
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController

class VideoPlayFragment : Fragment() {
    lateinit var binding: FragmentVideoPlayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPlayBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: VideoPlayFragmentArgs by navArgs()
        val videoUri = args.videoUri.toUri()

        binding.videoView.setVideoURI(videoUri)

        // Jab video prepare ho jaye to play karna start karo
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            binding.videoView.start()  // video play shuru karo
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
           findNavController().popBackStack()
        }

    }

}