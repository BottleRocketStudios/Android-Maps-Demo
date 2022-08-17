package com.bottlerocketstudios.mapsdemo.data.di

import com.bottlerocketstudios.mapsdemo.data.implementations.YelpRepositoryImplementation
import com.bottlerocketstudios.mapsdemo.data.network.YelpService
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

private enum class KoinNamedNetwork {
    Yelp
}

object NetworkModule {
    val module = module {
        single(named(KoinNamedNetwork.Yelp)) {
            provideYelpRetrofit(
                okHttpClient = get(named(KoinNamedNetwork.Yelp)),
                moshi = get()
            )
        }
        single { provideYelpService(get(named(KoinNamedNetwork.Yelp))) }
    }

}

private fun provideYelpRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.yelp.com/v3")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

private fun provideYelpService(retrofit: Retrofit): YelpService {
    return retrofit.create(YelpService::class.java)
}
