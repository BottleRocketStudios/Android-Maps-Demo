package com.bottlerocketstudios.mapsdemo.domain.models
sealed class UserFacingError {
    data class GeneralError(
        val title: Int,
        val description: Int
    ) : UserFacingError()
    data class ApiError(
        val code: Int = 0,
        val title: Int,
        val description: Int
    ) : UserFacingError()
    object NoError : UserFacingError()
}
