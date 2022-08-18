package com.bottlerocketstudios.mapsdemo.data.di

import com.bottlerocketstudios.mapsdemo.data.implementations.YelpRepositoryImplementation
import com.bottlerocketstudios.mapsdemo.data.network.YelpServiceFactory
import com.bottlerocketstudios.mapsdemo.data.serialization.DateTimeAdapter
import com.bottlerocketstudios.mapsdemo.data.serialization.ProtectedPropertyAdapter
import com.bottlerocketstudios.mapsdemo.domain.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.infrastructure.coroutine.DispatcherProviderImplementation
import com.squareup.moshi.Moshi
import org.koin.dsl.module

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

