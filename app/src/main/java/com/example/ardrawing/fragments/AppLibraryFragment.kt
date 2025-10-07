package com.example.ardrawing.fragments

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ardrawing.R
import com.example.ardrawing.adapters.AppImagesAdapter
import com.example.ardrawing.adapters.CategoriesAdapter
import com.example.ardrawing.databinding.FragmentAppLibraryBinding
import com.example.ardrawing.utils.CommonUtils


class AppLibraryFragment : Fragment() {
    lateinit var binding: FragmentAppLibraryBinding
    lateinit var adapter: AppImagesAdapter
    private val imageUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppLibraryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appImages = CommonUtils.getImagesFromArDrawer(requireContext())

        adapter = AppImagesAdapter(imageUris)
        binding.libraryRecyclerView.apply {
            val orientation = resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@AppLibraryFragment.adapter
        }
        loadImages()
    }


    private fun loadImages() {
        // If you want to do it on background thread, use coroutine or thread
        // For simplicity, do on main thread (but better move to background)

        val images = CommonUtils.getImagesFromArDrawer(requireContext())

        imageUris.clear()
        imageUris.addAll(images)

        adapter.notifyDataSetChanged()  // Notify adapter that data changed
    }

}