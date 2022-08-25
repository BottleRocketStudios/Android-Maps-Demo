package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.mutableStateOf
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.google.android.gms.maps.model.LatLng

val googleMapScreenStateTest = GoogleMapScreenState(
    businessList =
    mutableStateOf(listOf(yelpTestCard)),
    dallasLatLng = LatLng(32.7767, -96.7970),
    mutableStateOf(UserFacingError.NoError),
    resetError = {},
    retrySearch = {},
    onCameraMoveSearch = { _, _ ->  },
    googleMarkers = mutableStateOf(emptyList())
)
