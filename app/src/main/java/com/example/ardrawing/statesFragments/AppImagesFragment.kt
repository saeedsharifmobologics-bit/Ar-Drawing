package com.example.ardrawing.statesFragments

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ardrawing.adapters.profileAdapter.AppImagesAdapter
import com.example.ardrawing.adsManger.ScreenStatusLogs
import com.example.ardrawing.databinding.FragmentAppImagesBinding
import com.example.ardrawing.utils.ProfileHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppImagesFragment : Fragment() {

    private lateinit var binding: FragmentAppImagesBinding
    private lateinit var adapter: AppImagesAdapter
    private val imageUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppImagesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ScreenStatusLogs.logScreenView("AppImagesFragment","AppImagesFragment")

        //  1. Empty adapter se pehle RecyclerView set karo
        adapter = AppImagesAdapter(imageUris)
        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

        binding.ImagesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@AppImagesFragment.adapter
            setHasFixedSize(true)
        }

        // 🧠 2. Background thread me images load karo
        lifecycleScope.launch(Dispatchers.IO) {
            val loadedImages = ProfileHelper.getImagesFromArDrawer(requireContext())
            withContext(Dispatchers.Main) {
                updateImageList(loadedImages)
            }
        }
    }

    private fun updateImageList(newList: List<Uri>) {
        imageUris.clear()
        imageUris.addAll(newList)
        adapter.notifyDataSetChanged()
    }
}
