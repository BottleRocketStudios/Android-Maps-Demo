package com.bottlerocketstudios.mapsdemo.domain.models

class ApiException(
    val code: Int,
    message: String,
    val responseBody: String,
) : RuntimeException(message)
