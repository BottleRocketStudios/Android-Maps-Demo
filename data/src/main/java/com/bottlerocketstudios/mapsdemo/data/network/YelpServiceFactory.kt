package com.bottlerocketstudios.mapsdemo.data.network

import okhttp3.Interceptor
import retrofit2.Retrofit

class YelpServiceFactory: ServiceFactory() {
    override val baseUrl: String = "https://api.yelp.com/v3"
    override val headerInterceptor: Interceptor = YelpApiKeyInterceptor()

    internal fun produce(): YelpService = retrofit.create(YelpService::class.java)
}
