package com.example.ardrawing.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ardrawing.adapters.ArDrawingDataAdapter

import com.example.ardrawing.databinding.FragmentViewCategoryBinding
import com.example.ardrawing.data.ArDrawingData
import com.example.ardrawing.utils.ImageUrlList.getDataByCategory
import kotlinx.coroutines.launch
import kotlin.apply


class ViewCategoryFragment : Fragment() {
    lateinit var binding: FragmentViewCategoryBinding
    lateinit var adapter: ArDrawingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewCategoryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ViewCategoryFragmentArgs by navArgs()
        val category = args.categories

        val urlList: List<ArDrawingData> = getDataByCategory(category)




        adapter = ArDrawingDataAdapter(urlList, emptyList(), requireContext())

        binding.viewFavouriteUrlRV.apply {
            val orientation = requireContext().resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@ViewCategoryFragment.adapter
        }


    }

}
