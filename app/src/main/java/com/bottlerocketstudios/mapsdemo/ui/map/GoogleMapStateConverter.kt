package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.map.GoogleMapScreenState
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker

@Composable
fun YelpViewModel.toState() = GoogleMapScreenState(
    businessList = yelpBusinessState.collectAsState(emptyList()),
    dallasLatLng = dallasLatLng,
    yelpError = errorStateFlow.collectAsState(UserFacingError.NoError),
    resetError = ::resetError,
    retrySearch = ::retrySearch,
    onCameraMoveSearch = ::getYelpBusinessesOnMapMove,
    googleMarkers = googleMapsMarkersLatLng.collectAsState(emptyList()),
    setSelectedMarker = ::setSelectedMarker,
    yelpMarkerSelected = selectedMarker.collectAsState(YelpMarker(latitude = 0.0, longitude = 0.0, businessName = ""))
)
