package com.bottlerocketstudios.mapsdemo.data.model

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.Coordinates
import com.bottlerocketstudios.mapsdemo.domain.models.CoordinatesDto
import com.bottlerocketstudios.mapsdemo.domain.models.Dto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BusinessDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val businessName: String,
    @Json(name = "image_url") val imageUrl: String,
    @Json(name = "coordinates") val coordinates: CoordinatesDto,
) : Dto

fun BusinessDto.convertToBusiness() = Business(
    id = id,
    businessName = businessName,
    imageUrl = imageUrl,
    coordinates = coordinates.convertToCoordinates(),
)

private fun CoordinatesDto.convertToCoordinates() = Coordinates(
    latitude = latitude,
    longitude = longitude,
)
