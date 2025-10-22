package com.sketchbox.drawingapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.sketchbox.drawingapp.adapters.ArDrawingDataAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.adsManger.adsUtils
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.dataClass.ArDrawingData
import com.sketchbox.drawingapp.databinding.FragmentViewCategoryBinding
import com.sketchbox.drawingapp.utils.ImageUrlList.getDataByCategory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewCategoryFragment : Fragment() {

    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var adapter: ArDrawingDataAdapter
    private val viewModel: ArDrawingViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!Utils.subscriptionState) {
            adsUtils.loadNativeAd(binding.root,requireContext())
        }
        ScreenStatusLogs.logScreenView("ViewCategoryFragment","ViewCategoryFragment")

        val args: ViewCategoryFragmentArgs by navArgs()
        val category = args.categories

        // URL list by category
        val urlList: List<ArDrawingData> = getDataByCategory(category)

        //Initialize adapter with empty favourite list initially
        adapter = ArDrawingDataAdapter(
            favouriteUrllist = urlList,
            dbList = emptyList(),
            context = requireContext(),
            viewModel = viewModel,
            screenName = "ViewCategoryFragment"
        )

        //RecyclerView setup
        binding.viewFavouriteUrlRV.apply {
            val orientation = resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@ViewCategoryFragment.adapter
        }

        // Observe favourite list from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteList.collectLatest { favList ->
                // update adapter's favourite list when data changes
                adapter.updateDbList(favList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
