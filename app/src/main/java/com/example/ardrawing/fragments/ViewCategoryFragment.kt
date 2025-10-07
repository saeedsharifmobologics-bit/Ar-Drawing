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
import kotlinx.coroutines.launch
import kotlin.apply


class ViewCategoryFragment : Fragment() {

    lateinit var binding: FragmentViewCategoryBinding
    lateinit var adapter: ArDrawingDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


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
        val category = args.categories// isko change kar ke "fruits" ya "animals" bhi kar sakte hain

        val urlList: List<ArDrawingData> = when (category) {
            "Anime" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/anime_sketch.jpg"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/anime_sketch_1.jpg")
            )

            "fruits" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/apple_1.jpg"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/apple_2.jpg")
            )

            "Anatomy" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/anatomy.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/anatomy_2.jpg")
            )

            "Object" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/jug_object.jpg"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/building%20sketch.jpg"),
            )
            "Animal" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds%20image.jpg"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/lion%20image.jpg"),
            )
            "Car" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/car_sketch.jpg"),
            )
            "Person" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/person_sketch.jpg"),
            )

            else -> emptyList() // agar koi category match nahi karti
        }

        adapter = ArDrawingDataAdapter(urlList, emptyList(), requireContext())

        binding.viewFavouriteUrlRV.apply {
            val orientation = requireContext().resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@ViewCategoryFragment.adapter
        }


    }

}
