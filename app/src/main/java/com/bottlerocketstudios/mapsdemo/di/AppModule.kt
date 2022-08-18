package com.bottlerocketstudios.mapsdemo.di

import com.bottlerocketstudios.mapsdemo.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.mapsdemo.domain.di.BuildConfigProvider
import com.bottlerocketstudios.mapsdemo.ui.map.YelpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val module = module {
        single<BuildConfigProvider> { BuildConfigProviderImpl() }
        viewModel { YelpViewModel() }
    }
}
