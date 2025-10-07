package com.example.ardrawing.koinModule

import com.example.ardrawing.buinesslogiclayer.ArDrawingViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ArDrawingViewmodel() } // No constructor params? Great.
}