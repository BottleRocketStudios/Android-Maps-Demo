package com.bottlerocketstudios.mapsdemo.domain.models

import androidx.annotation.StringRes

sealed class UserFacingError : DomainModel {
    data class GeneralError(
        @StringRes val title: Int,
        @StringRes val description: Int
    ) : UserFacingError()
    data class ApiError(
        val code: Int = 0,
        @StringRes val title: Int,
        @StringRes val description: Int
    ) : UserFacingError()
    object NoError : UserFacingError()
}
