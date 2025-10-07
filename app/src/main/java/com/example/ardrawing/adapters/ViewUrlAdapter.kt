package com.example.ardrawing.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.ardrawing.R
import com.example.ardrawing.data.ArDrawingData
import com.example.ardrawing.fragments.ViewCategoryFragmentDirections
import com.example.ardrawing.utils.ImageHolder
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import androidx.core.graphics.toColorInt

class ArDrawingDataAdapter(
    private var favouriteUrllist: List<ArDrawingData>,
    private var dbList: List<ArDrawingData>,
    private val context: Context,
) : RecyclerView.Adapter<ArDrawingDataAdapter.ArDrawingDataHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ArDrawingDataHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_image_by_url, parent, false)
    )

    override fun onBindViewHolder(holder: ArDrawingDataHolder, position: Int) {
        val data = favouriteUrllist[position]
        val imageUrl = data.favouritefavouriteUrl

        val shimmerLayout =
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
        holder.image.visibility = View.INVISIBLE

        if (!imageUrl.isNullOrBlank()) {
            holder.image.load(imageUrl) {
                crossfade(true)
                allowHardware(false)
                transformations(RoundedCornersTransformation(16f))

                listener(
                    onSuccess = { _, _ ->
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        holder.image.visibility = View.VISIBLE
                    },
                    onError = { _, _ ->
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        holder.image.setImageResource(R.drawable.place_holder_img)
                        holder.image.visibility = View.VISIBLE
                    }
                )
            }
        } else {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            holder.image.setImageResource(R.drawable.place_holder_img)
            holder.image.visibility = View.VISIBLE
        }

        holder.image.setOnClickListener {
            val drawable = holder.image.drawable?.toBitmap()
            drawable?.let {
                ImageHolder.bitmap = it
                val action =
                    ViewCategoryFragmentDirections.actionViewCategoryFragmentToSelectionModeFragment()
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = favouriteUrllist.size

    class ArDrawingDataHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: AppCompatImageView = view.findViewById(R.id.ArDrawingDataImage)
    }

    fun updateData(newList: List<ArDrawingData>) {
        favouriteUrllist = newList
        notifyDataSetChanged()
    }

}
