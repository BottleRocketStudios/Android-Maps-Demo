package com.bottlerocketstudios.mapsdemo.infrastructure.retrofitadapter

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class ResultCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<Any, Call<Result<*>>> {
    override fun responseType(): Type = responseType
    override fun adapt(call: Call<Any>): Call<Result<*>> = ResultCall(call) as Call<Result<*>>
}
