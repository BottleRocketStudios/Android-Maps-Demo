package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.map.GoogleMapScreenState

@Composable
fun YelpViewModel.toState() = GoogleMapScreenState(
    businessList = yelpBusinessState.collectAsState(emptyList()),
    dallasLatLng = dallasLatLng
)
