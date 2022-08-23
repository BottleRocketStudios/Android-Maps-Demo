package com.bottlerocketstudios.mapsdemo.domain.models

sealed class UserFacingError {
    data class GeneralError(val title: String = "General Error", val description: String = "Something went wrong."): UserFacingError()
    data class NetworkError(val title: String = "Network Error", val description: String = "Unable to access the internet"): UserFacingError()
}
