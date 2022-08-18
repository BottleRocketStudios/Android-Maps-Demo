package com.bottlerocketstudios.mapsdemo.data.network

import com.bottlerocketstudios.mapsdemo.infrastructure.retrofitadapter.ResultRetrofitAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

abstract class ServiceFactory: KoinComponent {
    // DI
    private val moshi: Moshi by inject()

    //Configurable URL
    abstract val baseUrl: String

    abstract val headerInterceptor: Interceptor

    protected val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ResultRetrofitAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(3000L, TimeUnit.MILLISECONDS)
            .readTimeout(3000L, TimeUnit.MILLISECONDS)
            .writeTimeout(3000L, TimeUnit.MILLISECONDS)
            .addInterceptor(headerInterceptor)
            .build()
    }

}
