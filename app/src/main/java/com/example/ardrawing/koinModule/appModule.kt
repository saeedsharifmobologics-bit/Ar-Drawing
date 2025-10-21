package com.example.ardrawing.koinModule

import androidx.room.Room
import com.example.ardrawing.buinesslogiclayer.ArDrawingViewmodel
import com.example.ardrawing.dbUtils.ArDrawingDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    //Room Database
    single {
        Room.databaseBuilder(androidContext(), ArDrawingDatabase::class.java, "favorites_db").fallbackToDestructiveMigration(false).build()
    }


    single {
        get<ArDrawingDatabase>().arDrawingDataDao()
     }

    viewModel { ArDrawingViewmodel(get()) } //No constructor params? Great.
}