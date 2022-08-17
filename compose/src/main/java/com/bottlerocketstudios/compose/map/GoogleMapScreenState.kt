package com.bottlerocketstudios.compose.map

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import androidx.compose.runtime.State

data class GoogleMapScreenState(
    val businessList: State<List<Business>>
)
