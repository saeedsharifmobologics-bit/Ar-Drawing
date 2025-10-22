package com.sketchbox.drawingapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adapters.CategoriesAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.adsManger.adsUtils
import com.sketchbox.drawingapp.databinding.FragmentCategoriesBinding
import com.sketchbox.drawingapp.arHelper.CategoriesItem
import com.sketchbox.drawingapp.utils.CommonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.apply


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

        if (!Utils.subscriptionState) {
            adsUtils.loadNativeAd(binding.root,requireContext())
        }
        ScreenStatusLogs.logScreenView("CategoriesFragment","CategoriesFragment")

        val clockwiseAnimation= CommonUtils.getRotateClockwiseAnimation(requireContext())
        val anticlockwiseAnimation= CommonUtils.getRotateAnticlockwiseAnimation(requireContext())
        // Pehle empty list se adapter set karo taake UI jaldi render ho
        categoriesAdapter = CategoriesAdapter(emptyList())
        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

        binding.catogoriesRV.apply {
            startloading(clockwiseAnimation,anticlockwiseAnimation)
            layoutManager = GridLayoutManager(requireContext(), spanCount, RecyclerView.VERTICAL, false)
            adapter = categoriesAdapter
            setHasFixedSize(true)
        }

        // Phir 500 milliseconds ke baad data load karo aur adapter ko update karo
        lifecycleScope.launch {
            delay(600)  // ya agar 5ms chahiye to delay(5)
            val categoryList = withContext(Dispatchers.IO) {
                listOf(
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
                    CategoriesItem("Toys", R.drawable.toy_04)
                )
            }

            stoploading()
            categoriesAdapter.updateList(categoryList)
        }
    }

    fun startloading(clockwiseAnimation: Animation, anticlockwiseAnimation: Animation) {
        binding.innerProgressBar.startAnimation(clockwiseAnimation)
        binding.outerProgressBar.startAnimation(anticlockwiseAnimation)
        binding.innerProgressBar.visibility=View.VISIBLE
        binding.outerProgressBar.visibility=View.VISIBLE
    }
    fun stoploading() {

        binding.innerProgressBar.visibility=View.GONE
        binding.outerProgressBar.visibility=View.GONE
    }


    override fun onResume() {
        super.onResume()
    }
}