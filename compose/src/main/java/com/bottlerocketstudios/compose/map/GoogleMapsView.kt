package com.bottlerocketstudios.compose.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.alertdialog.CustomAlertDialog
import com.bottlerocketstudios.compose.utils.Preview
import com.bottlerocketstudios.compose.yelp.RetryButton
import com.bottlerocketstudios.compose.yelp.YelpBusinessList
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// https://developers.google.com/maps/documentation/android-sdk/views#zoom
private const val MAX_ZOOM_LEVEL = 15f // Street level
private const val MIN_ZOOM_LEVEL = 5f // Landmass/Continent
private const val CITY_ZOOM_LEVEL = 11f

@Composable
fun GoogleMapsView(googleMapScreenState: GoogleMapScreenState, toolbarEnabled: Boolean = false, modifier: Modifier) {
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = MAX_ZOOM_LEVEL, minZoomPreference = MIN_ZOOM_LEVEL)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(MapUiSettings(mapToolbarEnabled = toolbarEnabled))
    }
    // Observing and controlling the camera's state can be done with a CameraPositionState
    val googleCameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(googleMapScreenState.dallasLatLng, CITY_ZOOM_LEVEL)
    }
    val dialogVisibility = remember {
        mutableStateOf(value = false)
    }

    val fullScreenMaps = remember {
        mutableStateOf(value = false)
    }

    val showRetryButton = remember {
        mutableStateOf(value = false)
    }

    val markerSelected = remember {
        mutableStateOf(value = false)
    }

    dialogVisibility.value = googleMapScreenState.yelpError.value != UserFacingError.NoError
    fullScreenMaps.value = googleMapScreenState.businessList.value.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        if(showRetryButton.value) {
            RetryButton(retry = {
                showRetryButton.value = false
                googleMapScreenState.retrySearch()
            })
        }
        GoogleMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            modifier = if (fullScreenMaps.value) Modifier.fillMaxSize() else Modifier
                .height(400.dp)
                .fillMaxWidth(),
            cameraPositionState = googleCameraPositionState
        ) {
            if(googleMapScreenState.googleMarkers.value.isNotEmpty()) {
                addMarkers(yelpMarkers = googleMapScreenState.googleMarkers.value)
            }

            if(googleCameraPositionState.isMoving &&
                googleCameraPositionState.cameraMoveStartedReason.value == CameraMoveStartedReason.GESTURE.value) {
                val search = YelpLatLngSearch(
                    latitude = googleCameraPositionState.position.target.latitude,
                    longitude = googleCameraPositionState.position.target.longitude
                )
                googleMapScreenState.onCameraMoveSearch(search, googleCameraPositionState.position.zoom)
            }
        }

        AnimatedVisibility(dialogVisibility.value) {
            ShowErrorDialog(yelpError = googleMapScreenState.yelpError.value,
                onDismiss = {
                    googleMapScreenState.resetError()
                    showRetryButton.value = true
                })
        }

        Button(onClick = {
            mapUiSettings = mapUiSettings.copy(
                mapToolbarEnabled = !mapUiSettings.mapToolbarEnabled
            )
        }) {
            Text(text = stringResource(R.string.toggle_map_toolbar))
        }

        AnimatedVisibility(googleMapScreenState.businessList.value.isNotEmpty()) {
            YelpBusinessList(businessList = googleMapScreenState.businessList.value)
        }
    }
}

@Composable
fun ShowErrorDialog(yelpError: UserFacingError, onDismiss: () -> Unit) {
    when (yelpError) {
        is UserFacingError.ApiError -> CustomAlertDialog(
            modifier = Modifier,
            title = yelpError.title,
            message = yelpError.description,
            onDismiss = onDismiss
        )
        is UserFacingError.GeneralError -> CustomAlertDialog(
            modifier = Modifier,
            title = yelpError.title,
            message = yelpError.description,
            onDismiss = onDismiss
        )
        else -> {
            onDismiss()
        }
    }
}
@Composable
fun addMarkers(yelpMarkers: List<YelpMarker>, ) {
    yelpMarkers.forEach { yelpMarker ->
        Marker(
            state = MarkerState(
                position = LatLng(yelpMarker.latitude, yelpMarker.longitude)),
            title = yelpMarker.businessName,
        )
    }
}

@Preview
@Composable
fun GoogleMapPreview() {
    Preview {
        GoogleMapsView(modifier = Modifier.fillMaxSize(), googleMapScreenState = googleMapScreenStateTest)
    }
}
