package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng

val googleMapScreenStateTest = GoogleMapScreenState(
    businessList =
    mutableStateOf(listOf(yelpTestCard)),
    dallasLatLng = LatLng(32.7767, -96.7970)
)
