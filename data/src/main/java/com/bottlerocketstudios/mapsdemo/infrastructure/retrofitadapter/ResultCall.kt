package com.bottlerocketstudios.mapsdemo.infrastructure.retrofitadapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun <T> Response<T>.toResult(): Result<T> = if (isSuccessful) Result.success(body()!!) else Result.failure(HttpException(this))

class ResultCall<T>(private val delegate: Call<T>) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    callback.onResponse(this@ResultCall, Response.success(response.code(), response.toResult()))
                }

                override fun onFailure(call: Call<T>, throwable: Throwable) {
                    callback.onResponse(this@ResultCall, Response.success(Result.failure(throwable)))
                }
            }
        )
    }

    override fun cancel() = delegate.cancel()
    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())
    override fun execute(): Response<Result<T>> = Response.success(delegate.execute().toResult())
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
