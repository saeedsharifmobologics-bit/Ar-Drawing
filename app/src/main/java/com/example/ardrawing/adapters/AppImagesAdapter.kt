package com.example.ardrawing.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ardrawing.databinding.ViewImageByUrlBinding


class AppImagesAdapter(
    private val imageUris: List<Uri>
) : RecyclerView.Adapter<AppImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ViewImageByUrlBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ViewImageByUrlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = imageUris[position]
        holder.binding.ArDrawingDataImage.load(uri) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
            transformations(RoundedCornersTransformation(16f))
            error(android.R.color.holo_red_light)
        }
    }

    override fun getItemCount(): Int = imageUris.size
}
