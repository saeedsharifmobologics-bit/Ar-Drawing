package com.example.ardrawing.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ardrawing.R
import com.example.ardrawing.adapters.CategoriesAdapter
import com.example.ardrawing.databinding.FragmentCategoriesBinding
import com.example.ardrawing.arHelper.CategoriesItem
import kotlin.apply
import kotlin.toString


class CategoriesFragment : Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    lateinit var categoriesAdapter: CategoriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList = listOf(
            CategoriesItem("Birds", R.drawable.birds_04),
            CategoriesItem("Boats", R.drawable.boats_08),
            CategoriesItem("Characters", R.drawable.character_08),
            CategoriesItem("Trees", R.drawable.tree34),
            CategoriesItem("Vegetables", R.drawable.vegetable19),


            CategoriesItem("Animals", R.drawable.animals_11),
            CategoriesItem("Bacteria", R.drawable.bacteria_6),
            CategoriesItem("Fruits", R.drawable.fruits_24),
            CategoriesItem("Human Organs", R.drawable.humanorgans_02),
            CategoriesItem("Pirates", R.drawable.pirats_08),


            CategoriesItem("Plants", R.drawable.plants_24),
            CategoriesItem("Toys", R.drawable.toy_04),


        )
        // Set up adapter
        categoriesAdapter = CategoriesAdapter(categoryList)
        binding.catogoriesRV.apply {
            val orientation = resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2


            layoutManager = GridLayoutManager(requireContext(), spanCount, RecyclerView.VERTICAL, false)
            adapter = categoriesAdapter

        }
        binding.searchBar.addTextChangedListener { editable ->
            val querry = editable.toString()
            categoriesAdapter.filter(querry)
        }


    }


}