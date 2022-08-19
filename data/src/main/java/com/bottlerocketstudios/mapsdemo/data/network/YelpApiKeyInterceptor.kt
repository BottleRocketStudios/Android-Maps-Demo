package com.bottlerocketstudios.mapsdemo.data.network

import com.bottlerocketstudios.mapsdemo.domain.di.BuildConfigProvider
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class YelpApiKeyInterceptor() : Interceptor, KoinComponent {
    // DI
    private val buildConfigProvider: BuildConfigProvider by inject()

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "bearer " + buildConfigProvider.yelpApiKey).build()
        )
}
