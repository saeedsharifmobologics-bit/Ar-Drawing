package com.sketchbox.drawingapp.adapters.profileAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sketchbox.drawingapp.databinding.ItemThumbnailBinding
import com.sketchbox.drawingapp.statesFragments.ImageItem

class ThumbnailAdapter(
    private var items: List<ImageItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.thumbImage.setImageURI(item.uri)
        holder.binding.thumbBorder.visibility =
            if (item.isSelected) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) onItemClick(pos)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newList: List<ImageItem>) {
        items = newList
        notifyDataSetChanged()
    }

    fun highlightItem(selectedUri: Uri) {
        items.forEach { it.isSelected = it.uri == selectedUri }
        notifyDataSetChanged()
    }
}
