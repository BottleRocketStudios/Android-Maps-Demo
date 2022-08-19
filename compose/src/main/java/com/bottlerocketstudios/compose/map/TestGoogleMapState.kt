package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.bottlerocketstudios.compose.utils.asMutableState
import com.google.android.gms.maps.model.LatLng

val googleMapScreenStateTest = GoogleMapScreenState ( businessList =
    mutableStateOf(listOf(yelpTestCard)), dallasLatLng = LatLng(32.7767, -96.7970)
)
