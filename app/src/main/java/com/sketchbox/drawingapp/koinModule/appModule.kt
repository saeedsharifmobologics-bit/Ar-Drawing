package com.sketchbox.drawingapp.koinModule

import androidx.room.Room
import com.sketchbox.drawingapp.buinesslogiclayer.ArDrawingViewmodel
import com.sketchbox.drawingapp.dbUtils.ArDrawingDatabase
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