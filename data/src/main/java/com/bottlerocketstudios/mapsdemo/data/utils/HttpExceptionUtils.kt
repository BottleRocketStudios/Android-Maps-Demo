package com.bottlerocketstudios.mapsdemo.data.utils

import com.bottlerocketstudios.mapsdemo.domain.models.ApiException
import retrofit2.HttpException

fun HttpException.toApiException() = ApiException(code(), message(), response()?.errorBody().toString())
