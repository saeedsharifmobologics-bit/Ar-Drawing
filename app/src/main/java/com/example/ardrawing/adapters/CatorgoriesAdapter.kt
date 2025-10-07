package com.example.ardrawing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ardrawing.R
import com.example.ardrawing.arHelper.CategoriesItem
import com.example.ardrawing.fragments.CategoriesFragmentDirections

class CategoriesAdapter(
    var items: List<CategoriesItem>,
) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var filteredList = items.toMutableList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.categories_item_list, parent, false)
    )


    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        val items = filteredList[position]
        holder.category.setText(items.categoryName)
        holder.categoryImage.setImageResource(items.categoriesImage)
        holder.maincard.setOnClickListener {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToViewCategoryFragment(items.categoryName)
            holder.itemView.findNavController().navigate(action)

        }

    }

    override fun getItemCount(): Int = filteredList.size


    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category = view.findViewById<TextView>(R.id.catogoriesItemName)
        val categoryImage = view.findViewById<ImageView>(R.id.catogoriesItemImage)
        val maincard = view.findViewById<ConstraintLayout>(R.id.main_card_item)

    }

    fun filter(query: String) {
        val lowerQuery = query.lowercase().trim()
        filteredList = if (lowerQuery.isEmpty()) {
            items.toMutableList()
        } else {
            items.filter { it.categoryName.lowercase().contains(lowerQuery) }.toMutableList()
        }
        notifyDataSetChanged()
    }


}