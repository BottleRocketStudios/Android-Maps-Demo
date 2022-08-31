package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.mutableStateOf
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker

val googleMapScreenStateTest = GoogleMapScreenState(
    businessList =
    mutableStateOf(listOf(yelpTestCard)),
    dallasLatLng = LatLong(32.7767, -96.7970),
    mutableStateOf(UserFacingError.NoError),
    resetError = {},
    retrySearch = { _, _ -> },
    onCameraMoveSearch = { _, _ -> },
    googleMarkers = mutableStateOf(emptyList()),
    setSelectedMarker = {},
    yelpMarkerSelected = mutableStateOf(YelpMarker(latitude = 0.0, longitude = 0.0, businessName = ""))
)
