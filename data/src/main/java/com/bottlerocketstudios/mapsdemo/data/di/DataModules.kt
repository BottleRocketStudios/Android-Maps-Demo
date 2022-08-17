package com.bottlerocketstudios.mapsdemo.data.di

import com.bottlerocketstudios.mapsdemo.data.implementations.YelpRepositoryImplementation
import com.bottlerocketstudios.mapsdemo.data.network.YelpApiKeyInterceptor
import com.bottlerocketstudios.mapsdemo.data.network.YelpService
import com.bottlerocketstudios.mapsdemo.data.network.YelpServiceFactory
import com.bottlerocketstudios.mapsdemo.data.serialization.DateTimeAdapter
import com.bottlerocketstudios.mapsdemo.data.serialization.ProtectedPropertyAdapter
import com.bottlerocketstudios.mapsdemo.data.viewmodels.YelpViewModel
import com.bottlerocketstudios.mapsdemo.domain.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.infrastructure.coroutine.DispatcherProviderImplementation
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object DataModule {
    val module = module {
        single<Moshi> { Moshi.Builder().add(DateTimeAdapter(clock = get())).add(ProtectedPropertyAdapter()).build() }
        single<YelpRepository>{ YelpRepositoryImplementation() }
        single<DispatcherProvider>{ DispatcherProviderImplementation() }
    }
}

object NetworkModule {
    val module = module {
        single { YelpServiceFactory().produce() }
    }
}

object ViewModelModule {
    val module = module {
        viewModel { YelpViewModel() }
    }
}
