package com.bottlerocketstudios.mapsdemo.data.utils

import retrofit2.HttpException

fun <T> Result<T>.mapErrors(): Result<T> {
    return when (val exception = exceptionOrNull()) {
        is HttpException -> Result.failure(exception.toApiException())
        else -> this
    }
}
