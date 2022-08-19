package com.bottlerocketstudios.compose.map

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import androidx.compose.runtime.State
import com.google.android.gms.maps.model.LatLng

data class GoogleMapScreenState(
    val businessList: State<List<Business>>,
    val dallasLatLng: LatLng
)
