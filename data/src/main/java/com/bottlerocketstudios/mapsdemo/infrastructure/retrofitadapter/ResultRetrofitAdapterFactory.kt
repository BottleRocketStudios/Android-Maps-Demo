package com.bottlerocketstudios.mapsdemo.infrastructure.retrofitadapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultRetrofitAdapterFactory : CallAdapter.Factory() {

    private companion object {
        const val FIRST_INDEX = 0
    }

    // Handle the return type if it is a Kotlin.Result or null if it cannot be handled by the factory.

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }
        val responseType = getParameterUpperBound(FIRST_INDEX, returnType)

        return if (responseType is ParameterizedType && responseType.rawType == Result::class.java) {
            ResultCallAdapter<Any>(getParameterUpperBound(FIRST_INDEX, responseType))
        } else {
            null
        }
    }
}
