package com.sketchbox.drawingapp.statesFragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adapters.profileAdapter.ImageCarouselAdapter
import com.sketchbox.drawingapp.adapters.profileAdapter.ThumbnailAdapter
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.databinding.FragmentViewImagesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewImagesFragment : Fragment() {

    private lateinit var binding: FragmentViewImagesBinding
    private val viewModel: ArDrawingViewmodel by viewModel()

    private lateinit var carouselAdapter: ImageCarouselAdapter
    private lateinit var thumbnailAdapter: ThumbnailAdapter

    private var imageItems: MutableList<ImageItem> = mutableListOf()
    private var selectedPosition = 0
    private var initialSelectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ViewImagesFragmentArgs by navArgs()
        initialSelectedUri = args.selectedImageUri.toUri()
        Log.d("ViewImagesFragment", "Initial selected URI: $initialSelectedUri")

        setupCarousel()
        setupThumbnailList()
        setupButtons()
        observeImages()
        observePositionSync()
    }

    private fun setupCarousel() {
        carouselAdapter = ImageCarouselAdapter(emptyList())
        val carouselLayoutManager = CarouselLayoutManager()
        val snapHelper = CarouselSnapHelper()

        binding.imageCarousel.apply {
            layoutManager = carouselLayoutManager
            adapter = carouselAdapter
            snapHelper.attachToRecyclerView(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val snapView = snapHelper.findSnapView(carouselLayoutManager)
                        val pos = snapView?.let { carouselLayoutManager.getPosition(it) } ?: 0
                        if (pos != selectedPosition) {
                            selectedPosition = pos
                            viewModel.setCurrentPosition(pos)
                            updatePageNumber()
                        }
                    }
                }
            })
        }
    }

    private fun setupThumbnailList() {
        thumbnailAdapter = ThumbnailAdapter(emptyList()) { position ->
            viewModel.setCurrentPosition(position)
        }

        binding.rvThumbnails.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = thumbnailAdapter
        }
    }

    private fun observeImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appImagesList.collect { uriList ->
                    if (uriList.isEmpty()) {
                        findNavController().popBackStack()
                    }
                    imageItems = uriList.map { ImageItem(it) }.toMutableList()
                    carouselAdapter.updateList(imageItems.map { it.uri })
                    thumbnailAdapter.updateList(imageItems)

                    // initial URI handling
                    initialSelectedUri?.let { uri ->
                        val index = imageItems.indexOfFirst { it.uri == uri }
                        if (index != -1) {
                            selectedPosition = index
                            viewModel.setCurrentPosition(index)

                            // scroll carousel
                            binding.imageCarousel.scrollToPosition(index)

                            // ✅ Center selected thumbnail
                            val layoutManager =
                                binding.rvThumbnails.layoutManager as LinearLayoutManager
                            val recyclerWidth = binding.rvThumbnails.width
                            layoutManager.scrollToPositionWithOffset(index, recyclerWidth / 2)

                            thumbnailAdapter.highlightItem(uri)
                            updatePageNumber()
                        }
                        initialSelectedUri = null
                    }

                    if (initialSelectedUri == null && imageItems.isNotEmpty()) {
                        viewModel.setCurrentPosition(selectedPosition)
                    }
                }
            }
        }

        viewModel.loadImagesFromArDrawer(requireContext())
    }

    private fun observePositionSync() {
        viewModel.currentPosition.observe(viewLifecycleOwner) { pos ->
            if (pos in imageItems.indices) {
                selectedPosition = pos
                val uri = imageItems[pos].uri

                // scroll carousel
                binding.imageCarousel.smoothScrollToPosition(pos)

                // ✅ Center selected thumbnail
                val layoutManager = binding.rvThumbnails.layoutManager as LinearLayoutManager
                val recyclerWidth = binding.rvThumbnails.width
                layoutManager.scrollToPositionWithOffset(pos, recyclerWidth / 2)

                // highlight thumbnail
                thumbnailAdapter.highlightItem(uri)
                updatePageNumber()
            }
        }
    }

    private fun updatePageNumber() {
        val total = imageItems.size
        val current = selectedPosition + 1
        binding.tvPageNumber.text = "$current / $total"
    }

    private fun setupButtons() {
        binding.btnClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnDelete.setOnClickListener {
            if (selectedPosition in imageItems.indices) {
                val uriToDelete = imageItems[selectedPosition].uri
                deleteImage(uriToDelete)
            }
        }

    }

    private fun deleteImage(uri: Uri) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.delete_image))
            .setMessage(requireContext().getString(R.string.delete_image_dec))
            .setPositiveButton(requireContext().getString(R.string.yes_dialog)) { dialog, _ ->
                // User confirmed, delete the image
                requireContext().contentResolver.delete(uri, null, null)
                viewModel.loadImagesFromArDrawer(requireContext()) // reload images
                dialog.dismiss()
            }
            .setNegativeButton(requireContext().getString(R.string.no_dialog)) { dialog, _ ->
                // User cancelled
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

}
