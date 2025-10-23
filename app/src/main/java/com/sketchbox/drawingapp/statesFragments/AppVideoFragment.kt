package com.sketchbox.drawingapp.statesFragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sketchbox.drawingapp.adapters.profileAdapter.AppVideosAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.databinding.FragmentAppVideoBinding
import com.sketchbox.drawingapp.utils.ProfileHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppVideoFragment : Fragment() {

    private lateinit var binding: FragmentAppVideoBinding
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
        ScreenStatusLogs.logScreenView("AppVideoFragment","AppVideoFragment")

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        // Empty adapter pehle set karo (UI jaldi render hoga)
        videosAdapter = AppVideosAdapter(videoUris)
        binding.videoRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = videosAdapter
            setHasFixedSize(true)
        }

        // 🧠 2. Data background me load karo
        lifecycleScope.launch(Dispatchers.IO) {
            val loadedVideos = ProfileHelper.getVideoFromArDrawer(requireContext())
            withContext(Dispatchers.Main) {
                updateVideoList(loadedVideos)
            }
        }
    }

    private fun updateVideoList(newList: List<Uri>) {
        videoUris.clear()
        videoUris.addAll(newList)
        videosAdapter.notifyDataSetChanged()
    }
}
