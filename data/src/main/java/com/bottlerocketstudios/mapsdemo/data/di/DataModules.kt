package com.bottlerocketstudios.mapsdemo.data.di

import com.bottlerocketstudios.mapsdemo.data.implementations.YelpRepositoryImplementation
import com.bottlerocketstudios.mapsdemo.data.network.YelpApiKeyInterceptor
import com.bottlerocketstudios.mapsdemo.data.network.YelpService
import com.bottlerocketstudios.mapsdemo.data.network.YelpServiceFactory
import com.bottlerocketstudios.mapsdemo.data.serialization.DateTimeAdapter
import com.bottlerocketstudios.mapsdemo.data.serialization.ProtectedPropertyAdapter
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object DataModule {
    val module = module {
        single<Moshi> { Moshi.Builder().add(DateTimeAdapter(clock = get())).add(ProtectedPropertyAdapter()).build() }
        single<YelpRepository>{ YelpRepositoryImplementation() }
    }
}

object NetworkModule {
    val module = module {
        single { YelpServiceFactory().produce() }
    }
}
