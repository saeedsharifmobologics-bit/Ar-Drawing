package com.sketchbox.drawingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sketchbox.drawingapp.adapters.ArDrawingDataAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.adsManger.adsUtils
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.databinding.FragmentFavouriteBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private val viewmodel: ArDrawingViewmodel by viewModel()
    private lateinit var adapter: ArDrawingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        ScreenStatusLogs.logScreenView("FavouriteFragment", "FavouriteFragment")
        if (!Utils.subscriptionState) {
            adsUtils.loadNativeAd(requireView(), requireContext())

        }
        adapter = ArDrawingDataAdapter(
            emptyList(),
            emptyList(),
            requireContext(),
            viewmodel,
            "FavouriteFragment"
        )

        binding.favouriteRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            adapter = this@FavouriteFragment.adapter
        }

        // 🔹 Collect and update list when data changes
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.favoriteList.collectLatest { list ->
                adapter.updateList(list ?: emptyList())
            }
        }
    }
}
