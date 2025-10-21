package com.example.ardrawing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import com.example.ardrawing.R
import com.example.ardrawing.adapters.ArDrawingDataAdapter.ArDrawingDataHolder
import com.example.ardrawing.dataClass.ArDrawingData
import com.example.ardrawing.fragments.HomeFragmentDirections
import com.example.ardrawing.utils.CommonUtils
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout

class CategoryChildAdapter(
    private val urlList: MutableList<ArDrawingData>   // 👈 mutable list for better performance
) : RecyclerView.Adapter<ArDrawingDataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArDrawingDataHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_section_image, parent, false)
        return ArDrawingDataHolder(view)
    }

    override fun onBindViewHolder(holder: ArDrawingDataHolder, position: Int) {
        val data = urlList[position]
        val imageUrl = data.favouriteUrl

  /*      val shimmerLayout =
            holder.itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)

        // Create shimmer with custom colors
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor("#E0E0E0".toColorInt())       // Light gray base color
            .setHighlightColor("#F5F5F5".toColorInt())  // Lighter highlight color
            .setDuration(1500)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .build()

        shimmerLayout.setShimmer(shimmer)
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()

        // Hide image initially till loaded
        holder.image.visibility = View.INVISIBLE*/

        if (imageUrl.isNotBlank()) {
            holder.image.load(imageUrl) {
                allowHardware(false)
                crossfade(300) // smooth transition
                size(ViewSizeResolver(holder.image))
                scale(coil.size.Scale.FILL)
                listener(
                    onSuccess = { _, _ ->
                      /*  shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE*/
                        holder.image.visibility = View.VISIBLE
                    },
                    onError = { _, _ ->
                   /*     shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE*/
                        holder.image.setImageResource(R.drawable.place_holder_img)
                        holder.image.visibility = View.VISIBLE
                    }
                )
            }
        } else {
         /*   shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE*/
            holder.image.setImageResource(R.drawable.place_holder_img)
            holder.image.visibility = View.VISIBLE
        }

        // Image click → bitmap store + navigate
        holder.image.setOnClickListener {
            val drawable = holder.image.drawable?.toBitmap()
            drawable?.let {
                CommonUtils.ImageHolder.bitmap = it
                val action = HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount() = urlList.size


    fun updateData(newList: List<ArDrawingData>) {
        urlList.clear()
        urlList.addAll(newList)
        notifyDataSetChanged()
    }
}
