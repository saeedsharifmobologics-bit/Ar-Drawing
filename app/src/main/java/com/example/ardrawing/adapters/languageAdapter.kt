package com.example.ardrawing.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ardrawing.databinding.LanguageCardLayoutBinding

class LanguageAdapter(
    private val languages: List<Pair<String, String>>,
    private var selectedPosition: Int = 0,
    private val onSelect: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(val binding: LanguageCardLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(language: Pair<String, String>, position: Int) {
            binding.tvLanguage.text = language.first
            binding.rbLanguage.isChecked = (position == selectedPosition)

            // Card border highlight
            binding.root.strokeColor =
                if (position == selectedPosition) Color.BLACK
                else Color.LTGRAY

            binding.root.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onSelect(language.second)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LanguageCardLayoutBinding.inflate(inflater, parent, false)
        return LanguageViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position], position)
    }

    override fun getItemCount() = languages.size
}
