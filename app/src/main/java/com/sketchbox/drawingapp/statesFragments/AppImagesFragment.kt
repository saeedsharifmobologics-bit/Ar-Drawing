package com.sketchbox.drawingapp.statesFragments

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sketchbox.drawingapp.adapters.profileAdapter.AppImagesAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.databinding.FragmentAppImagesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppImagesFragment : Fragment() {

    private lateinit var binding: FragmentAppImagesBinding
    private lateinit var adapter: AppImagesAdapter
    private val viewModel: ArDrawingViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ScreenStatusLogs.logScreenView("AppImagesFragment", "AppImagesFragment")

        setupRecyclerView()
        setupListeners()
        observeImages()

        // ✅ ViewModel ko load karne do
        viewModel.loadImagesFromArDrawer(requireContext())
    }

    private fun setupRecyclerView() {
        adapter = AppImagesAdapter(emptyList(), viewModel)
        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

        binding.ImagesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@AppImagesFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appImagesList.collect { imageList ->
                    if (imageList.isEmpty()) {
                        binding.emptyView.visibility = View.VISIBLE
                    } else {
                        updateImageList(imageList)

                    }
                }
            }
        }
    }

    private fun updateImageList(newList: List<Uri>) {
        adapter.updateList(newList)
    }
}
