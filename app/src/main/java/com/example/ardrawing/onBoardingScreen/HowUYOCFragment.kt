package com.example.ardrawing.onBoardingScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ardrawing.R
import com.example.ardrawing.adsManger.ScreenStatusLogs
import com.example.ardrawing.utils.CommonUtils
import com.example.ardrawing.adsManger.adsUtils.loadNativeAd

class HowUYOCFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_how_u_y_o_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ScreenStatusLogs.logScreenView("HowUYOCFragment","HowUYOCFragment")
    }


}