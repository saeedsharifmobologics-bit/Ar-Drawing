package com.sketchbox.drawingapp.adapters.profileAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.sketchbox.drawingapp.R // apne app ka R import karna (android.R nahi)
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.databinding.ViewImageByUrlBinding
import com.sketchbox.drawingapp.statesFragments.AppImagesFragmentDirections

class AppImagesAdapter(
    private var imageUris: List<Uri>,
    private val viewModel: ArDrawingViewmodel
) : RecyclerView.Adapter<AppImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ViewImageByUrlBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ViewImageByUrlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = imageUris[position]

        holder.binding.favoriteBtn.visibility = View.GONE
        holder.binding.shimmerViewContainer.visibility = View.GONE

        holder.binding.ArDrawingDataImage.setPadding(0, 0, 0, 0)

        holder.binding.ArDrawingDataImage.setImageURI(uri)


        holder.binding.ArDrawingDataImage.setOnClickListener {
            viewModel.setCurrentPosition(position)  // current image index
            val action = AppImagesFragmentDirections
                .actionAppImagesFragmentToViewImagesFragment(uri.toString())

            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = imageUris.size

    fun updateList(newList: List<Uri>) {
        imageUris = newList
        notifyDataSetChanged()
    }
}
