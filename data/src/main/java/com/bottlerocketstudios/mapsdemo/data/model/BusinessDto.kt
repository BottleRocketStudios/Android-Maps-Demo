package com.bottlerocketstudios.mapsdemo.data.model

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.Coordinates
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BusinessDto(
    val id: String,
    @Json(name = "name")
    val businessName: String,
    @Json(name = "image_url")
    val imageUrl: String,
    val coordinates: Coordinates
): Dto

fun BusinessDto.convertToBusiness() = Business(
    id = id,
    businessName = businessName,
    imageUrl = imageUrl,
    coordinates = coordinates
)