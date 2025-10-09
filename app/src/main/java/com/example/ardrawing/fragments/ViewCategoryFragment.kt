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
        val category = args.categories// isko change kar ke "fruits" ya "animals" bhi kar sakte hain

        val urlList: List<ArDrawingData> = getDataByCategory(category)


        /* when (category) {
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
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds11.png")
        )

        "Boats" -> listOf( ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats17.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats18.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats19.png"))
        "Characters" -> listOf(  ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character01.png"),
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
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character16.png"))
        "Trees" -> listOf( ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree01.png"),
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
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree44.png")
        )
        "Vegetables" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable17.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable18.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable19.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable20.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable21.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable22.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable23.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable24.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable25.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable26.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable27.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable28.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable29.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable30.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable31.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable32.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable33.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable34.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable35.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable36.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable37.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable38.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable39.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable40.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable41.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable42.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable43.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable44.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable45.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable46.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable47.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable48.png")
        )
        "Animals" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals17.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals18.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals19.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals20.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals21.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals22.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals23.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals24.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals25.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals26.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals27.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals28.png")
        )

        "Bacteria" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria1.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria2.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria3.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria4.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria5.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria6.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria7.png")
        )

        "Fruits" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits17.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits18.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits19.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits20.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits21.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits22.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits23.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits24.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits25.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits26.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits27.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits28.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits29.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits30.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits31.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits32.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits33.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits34.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits35.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits36.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits37.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits38.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits39.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits40.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits41.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits42.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits43.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits44.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits45.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits46.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits47.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits48.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits49.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits50.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits51.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits52.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits53.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits54.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits55.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits56.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits57.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits58.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits59.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits60.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits61.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits62.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits63.png")
        )

        "Human Organs" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans17.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans18.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans19.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans20.png")
        )

        "Pirates" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats17.png")
        )

        "Plants" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants10.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants11.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants12.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants13.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants14.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants15.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants16.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants17.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants18.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants19.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants20.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants21.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants22.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants23.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants24.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants25.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants26.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants27.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants28.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants29.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants30.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants31.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants32.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants33.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants34.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants35.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants36.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants37.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants38.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants39.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants40.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants41.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants42.png")
        )

        "Toys" -> listOf(
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy01.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy02.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy03.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy04.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy05.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy06.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy07.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy08.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy09.png"),
            ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy10.png")
        )

        else -> emptyList()
    }*/


        adapter = ArDrawingDataAdapter(urlList, emptyList(), requireContext())

        binding.viewFavouriteUrlRV.apply {
            val orientation = requireContext().resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = this@ViewCategoryFragment.adapter
        }


    }

}
