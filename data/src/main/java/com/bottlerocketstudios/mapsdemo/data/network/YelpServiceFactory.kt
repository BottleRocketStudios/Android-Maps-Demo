package com.bottlerocketstudios.mapsdemo.data.network

import okhttp3.Interceptor

class YelpServiceFactory : ServiceFactory() {
    override val baseUrl: String = "https://api.yelp.com/v3/"
    override val headerInterceptor: Interceptor = YelpApiKeyInterceptor()

    internal fun produce(): YelpService = retrofit.create(YelpService::class.java)
}
