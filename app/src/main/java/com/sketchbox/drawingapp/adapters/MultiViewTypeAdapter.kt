package com.sketchbox.drawingapp.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sketchbox.drawingapp.dataClass.ArDrawingData
import com.sketchbox.drawingapp.databinding.CategorySectionImageBinding
import com.sketchbox.drawingapp.databinding.CatogoriesSectionBinding
import com.sketchbox.drawingapp.fragments.HomeFragmentDirections
import com.sketchbox.drawingapp.utils.CommonUtils

const val CATEGORY_NAME = 0
const val CATEGORY_IMAGE = 1

class MultiViewTypeAdapter(
    private val categoryList: List<Any>,  // mixed types (String + ArDrawingData)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CategoryNameViewHolder(val binding: CatogoriesSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryName: String) {
            binding.tvCategoryName.text = categoryName
            binding.tvSeeAll.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToViewCategoryFragment(categoryName)
                binding.root.findNavController().navigate(action)
            }
        }
    }

    inner class CategoryImageViewHolder(val binding: CategorySectionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(images: List<ArDrawingData>) {
            if (images.size >= 3) {
                binding.sectionImage1.load(
                    images[0].favouriteUrl
                )
                binding.sectionImage2.load(images[1].favouriteUrl)
                binding.sectionImage3.load(images[2].favouriteUrl)

                binding.sectionImage1.setOnClickListener {
                    navigateWithBitmap(
                        binding.sectionImage1.drawable,
                        binding
                    )
                }
                binding.sectionImage2.setOnClickListener {
                    navigateWithBitmap(
                        binding.sectionImage1.drawable,
                        binding
                    )
                }
                binding.sectionImage3.setOnClickListener {
                    navigateWithBitmap(
                        binding.sectionImage3.drawable,
                        binding
                    )
                }
            }
        }
    }


    // Common click handler
    fun navigateWithBitmap(drawable: Drawable?, binding1: CategorySectionImageBinding) {

        if (drawable == null) {
            Toast.makeText(
                binding1.root.context,
                "Please wait, image is still loading...",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            val bitmap = drawable.toBitmap()
            CommonUtils.ImageHolder.bitmap = bitmap
            CommonUtils.ImageHolder.pickLocation=null

            val action = HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
            binding1.root.findNavController().navigate(action)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                binding1.root.context,
                "Error loading image. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    override fun getItemViewType(position: Int): Int {
        return when (val item = categoryList[position]) {
            is String -> CATEGORY_NAME
            is List<*> -> CATEGORY_IMAGE    // ✅ fixed
            else -> throw IllegalArgumentException("Unknown type at position $position")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CATEGORY_NAME -> {
                val binding = CatogoriesSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CategoryNameViewHolder(binding)
            }

            CATEGORY_IMAGE -> {
                val binding = CategorySectionImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CategoryImageViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryNameViewHolder -> holder.bind(categoryList[position] as String)
            is CategoryImageViewHolder -> holder.bind(categoryList[position] as List<ArDrawingData>)
        }
    }


    override fun getItemCount() = categoryList.size
}
