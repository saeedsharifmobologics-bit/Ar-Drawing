package com.example.ardrawing.adapters.profileAdapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import coil.decode.VideoFrameDecoder
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ardrawing.databinding.AppVideoItemViewBinding
import com.example.ardrawing.statesFragments.AppVideoFragmentDirections

class AppVideosAdapter(private val videoUris: List<Uri>) :
    RecyclerView.Adapter<AppVideosAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolder {
        val layoutInflater =
            AppVideoItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(
        holder: VideoViewHolder,
        position: Int
    ) {
        val videoUri = videoUris[position]
        holder.binding.imageView.load(videoUri) {
            decoderFactory(VideoFrameDecoder.Factory())
            transformations(RoundedCornersTransformation(16f))

        }

        holder.binding.playButton.setOnClickListener {
            holder.itemView.findNavController().navigate(AppVideoFragmentDirections.actionAppVideoFragmentToVideoPlayFragment(videoUri.toString()))
        }


    }

    override fun getItemCount(): Int {
        Log.d("AppVideoSize", videoUris.size.toString())

        return videoUris.size
    }

    inner class VideoViewHolder(val binding: AppVideoItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)


}