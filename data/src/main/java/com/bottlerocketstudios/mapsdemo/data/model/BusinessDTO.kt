package com.bottlerocketstudios.mapsdemo.data.model

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.Coordinates
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BusinessDTO(
    val id: String,
    val businessName: String,
    @Json(name = "image_url" )
    val imageUrl: String,
    val coordinates: Coordinates
)

fun BusinessDTO.convertToBusiness() = Business(
    id = id,
    businessName = businessName,
    imageUrl = imageUrl,
    coordinates = coordinates
)
