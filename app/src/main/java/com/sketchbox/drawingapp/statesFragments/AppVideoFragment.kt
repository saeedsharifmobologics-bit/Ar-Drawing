package com.sketchbox.drawingapp.statesFragments

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
import com.sketchbox.drawingapp.adapters.profileAdapter.AppVideosAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.databinding.FragmentAppVideoBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class AppVideoFragment : Fragment() {

    private lateinit var binding: FragmentAppVideoBinding
    private val viewModel: ArDrawingViewmodel by viewModel()

    private lateinit var videosAdapter: AppVideosAdapter
    private val videoUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ScreenStatusLogs.logScreenView("AppVideoFragment", "AppVideoFragment")

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        videosAdapter = AppVideosAdapter(videoUris)
        binding.videoRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = videosAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appVideosList.collect { list ->
                    if (list.isEmpty()){
                        binding.emptyView.visibility=View.VISIBLE
                    }
                    updateVideoList(list)
                }
            }
        }

        viewModel.loadVideosFromArDrawer(requireContext())
    }

    private fun updateVideoList(newList: List<Uri>) {
        videoUris.clear()
        videoUris.addAll(newList)
        videosAdapter.notifyDataSetChanged()
    }
}
