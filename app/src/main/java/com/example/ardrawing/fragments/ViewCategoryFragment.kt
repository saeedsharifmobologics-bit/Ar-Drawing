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
            "Birds" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds01.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds02.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds03.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds04.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds05.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds06.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds07.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds08.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds09.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds10.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds11.png"),

                )

            "Boats" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat01.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat02.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat03.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat04.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat05.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat06.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat07.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat08.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat09.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat10.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat11.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat12.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat13.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat14.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat15.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat16.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat17.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat18.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boat19.png"),

                )

            "Characters" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character01.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character02.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character03.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character04.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character05.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character06.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character07.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character08.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character09.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character10.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character11.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character12.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character13.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character14.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character15.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character16.png"),

                )

            "Trees" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree01.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree02.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree03.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree04.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree05.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree06.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree07.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree08.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree09.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree10.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree11.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree12.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree13.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree14.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree15.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree16.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree17.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree18.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree19.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree20.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree21.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree22.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree23.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree24.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree25.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree26.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree27.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree28.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree29.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree30.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree31.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree32.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree33.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree34.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree35.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree36.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree37.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree38.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree39.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree40.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree41.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree42.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree43.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree44.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree45.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree46.png"),
            )

            "Vegetables" -> listOf(
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables01.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables02.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables03.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables04.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables05.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables06.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables07.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables08.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables09.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables10.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables11.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables12.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables13.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables14.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables15.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables16.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables17.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables18.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables19.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables20.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables21.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables22.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables23.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables24.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables25.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables26.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables27.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables28.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables29.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables30.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables31.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables32.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables33.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables34.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables35.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables36.png"),
                ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetables37.png"),

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
