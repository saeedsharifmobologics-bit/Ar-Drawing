package com.sketchbox.drawingapp.adapters.profileAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sketchbox.drawingapp.databinding.ItemImageSliderBinding

class ImageCarouselAdapter(
    private var images: List<Uri>
) : RecyclerView.Adapter<ImageCarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(val binding: ItemImageSliderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val uri = images[position]
        holder.binding.imageView.load(uri) { crossfade(true) }
    }

    override fun getItemCount() = images.size

    fun getItemAt(position: Int): Uri = images[position]

    fun updateList(newList: List<Uri>) {
        images = newList
        notifyDataSetChanged()
    }
}
