package com.bottlerocketstudios.compose.map

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import androidx.compose.runtime.State
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.model.LatLng

data class GoogleMapScreenState(
    val businessList: State<List<Business>>,
    val dallasLatLng: LatLong,
    val yelpError: State<UserFacingError>,
    val resetError: () -> Unit,
    val retrySearch: () -> Unit,
    val onCameraMoveSearch: (latLong: LatLong, zoomLevel: Float) -> Unit,
    val googleMarkers: State<List<YelpMarker>>,
    val setSelectedMarker: (yelpMarker: YelpMarker) -> Unit,
    val yelpMarkerSelected: State<YelpMarker>
)
