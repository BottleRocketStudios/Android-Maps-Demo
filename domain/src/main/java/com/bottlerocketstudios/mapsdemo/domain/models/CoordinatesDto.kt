package com.bottlerocketstudios.mapsdemo.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoordinatesDto(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
) : Dto
