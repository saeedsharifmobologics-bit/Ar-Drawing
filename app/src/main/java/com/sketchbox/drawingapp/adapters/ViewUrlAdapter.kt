package com.sketchbox.drawingapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.dataClass.ArDrawingData
import com.sketchbox.drawingapp.fragments.FavouriteFragmentDirections
import com.sketchbox.drawingapp.fragments.ViewCategoryFragmentDirections
import com.sketchbox.drawingapp.utils.CommonUtils

class ArDrawingDataAdapter(
    private var favouriteUrllist: List<ArDrawingData>,
    private var dbList: List<ArDrawingData>,
    private val context: Context,
    private val viewModel: ArDrawingViewmodel,
    private val screenName: String,
) : RecyclerView.Adapter<ArDrawingDataAdapter.ArDrawingDataHolder>() {
    override

    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ArDrawingDataHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_image_by_url, parent, false)
    )

    override fun onBindViewHolder(holder: ArDrawingDataHolder, position: Int) {
        val data = favouriteUrllist[position]
        val imageUrl = data.favouriteUrl

        val shimmerLayout =
            holder.itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)

        if (screenName == "FavouriteFragment") {
            holder.favoriteBtn.visibility = View.GONE
        }

        // Shimmer setup
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor("#E0E0E0".toColorInt())
            .setHighlightColor("#F5F5F5".toColorInt())
            .setDuration(1500)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .build()

        shimmerLayout.setShimmer(shimmer)
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()

        holder.image.visibility = View.INVISIBLE

        //Image load with Coil
        if (imageUrl.isNotBlank()) {
            holder.image.load(imageUrl) {
                allowHardware(false)
                size(ViewSizeResolver(holder.image))
                scale(coil.size.Scale.FILL)

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

        //Check agar image favourite mein hai to heart red karo
        val isFavourite = dbList.any { it.favouriteUrl == imageUrl }

        if (isFavourite) {
            holder.favoriteBtn.setBackgroundResource(R.drawable.favourite_ic)  // red heart
        } else {
            holder.favoriteBtn.setBackgroundResource(R.drawable.notfavourite_ic) // normal heart
        }

        holder.image.setOnClickListener {
            val drawable = holder.image.drawable?.toBitmap()
            drawable?.let {
                CommonUtils.ImageHolder.bitmap = it
                CommonUtils.ImageHolder.pickLocation = null
                if (screenName == "FavouriteFragment"){
                    val action= FavouriteFragmentDirections.actionFavouriteFragmentToSelectionModeFragment()
                    holder.itemView.findNavController().navigate(action)
                }
                else{
                    val action = ViewCategoryFragmentDirections.actionViewCategoryFragmentToSelectionModeFragment()
                    holder.itemView.findNavController().navigate(action)
                }



            }
        }



        holder.favoriteBtn.setOnClickListener {
            if (isFavourite) {
                viewModel.removeFavorite(imageUrl, { boolean ->
                    Log.d("DeletedStatus", boolean.toString())
                })
            } else {
                viewModel.addFavorite(imageUrl)
            }
        }
    }

    override fun getItemCount(): Int = favouriteUrllist.size

    class ArDrawingDataHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.ArDrawingDataImage)
        val favoriteBtn: AppCompatButton = view.findViewById(R.id.favoriteBtn)
    }


    fun updateList(newList: List<ArDrawingData>) {
        this.favouriteUrllist = newList
        notifyDataSetChanged()
    }

    fun updateDbList(newList: List<ArDrawingData>) {
        this.dbList = newList
        notifyDataSetChanged()
    }


}
