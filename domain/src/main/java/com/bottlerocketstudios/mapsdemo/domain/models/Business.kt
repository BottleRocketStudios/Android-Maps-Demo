package com.bottlerocketstudios.mapsdemo.domain.models

data class Business(
    val id: String,
    val businessName: String,
    val imageUrl: String,
    val coordinates: Coordinates,
) : DomainModel
