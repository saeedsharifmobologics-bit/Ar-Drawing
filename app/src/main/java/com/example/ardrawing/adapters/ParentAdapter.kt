package com.example.ardrawing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ardrawing.R
import com.example.ardrawing.data.CategoryModel

class ParentAdapter(
    private val parentList: List<CategoryModel>,
    private val onSeeAllClick: (String) -> Unit
) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.catogories_section, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ParentViewHolder,
        position: Int
    ) {
        val items = parentList[position]
        holder.categoryName.text = items.categoryName
        holder.categoryImageRv.apply {
            layoutManager = GridLayoutManager(holder.itemView.context, 3)
            adapter = CategoryChildAdapter(items.items)
            setHasFixedSize(true)
        }
        holder.categorySeeAll.setOnClickListener {
            onSeeAllClick(items.categoryName)
        }

    }

    override fun getItemCount() = parentList.size

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName = itemView.findViewById<TextView>(R.id.tvCategoryName)
        val categorySeeAll = itemView.findViewById<TextView>(R.id.tvSeeAll)
        val categoryImageRv = itemView.findViewById<RecyclerView>(R.id.rvChild)

    }
}