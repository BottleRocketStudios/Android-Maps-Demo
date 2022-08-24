package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.map.GoogleMapScreenState
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError

@Composable
fun YelpViewModel.toState() = GoogleMapScreenState(
    businessList = yelpBusinessState.collectAsState(emptyList()),
    dallasLatLng = dallasLatLng,
    yelpError = errorStateFlow.collectAsState(UserFacingError.NoError)
)
