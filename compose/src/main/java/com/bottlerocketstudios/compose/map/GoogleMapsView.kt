package com.bottlerocketstudios.compose.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.alertdialog.CustomAlertDialog
import com.bottlerocketstudios.compose.utils.Preview
import com.bottlerocketstudios.compose.utils.PreviewAllDevices
import com.bottlerocketstudios.compose.utils.map.MapClusterItem
import com.bottlerocketstudios.compose.utils.map.clusterList
import com.bottlerocketstudios.compose.utils.map.toMapClusterItems
import com.bottlerocketstudios.compose.yelp.RetryButton
import com.bottlerocketstudios.compose.yelp.YelpBusinessList
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithm
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithmAdapter
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber

// https://developers.google.com/maps/documentation/android-sdk/views#zoom
private const val MAX_ZOOM_LEVEL = 18f // Street level
private const val MIN_ZOOM_LEVEL = 5f // Landmass/Continent
private const val CITY_ZOOM_LEVEL = 11f
private var algorithm: ScreenBasedAlgorithm<MapClusterItem> = ScreenBasedAlgorithmAdapter(
    PreCachingAlgorithmDecorator(
        NonHierarchicalDistanceBasedAlgorithm()
    )
)

@Composable
fun GoogleMapsView(modifier: Modifier, googleMapScreenState: GoogleMapScreenState) {
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = MAX_ZOOM_LEVEL, minZoomPreference = MIN_ZOOM_LEVEL)
        )
    }

    // Observing and controlling the camera's state can be done with a CameraPositionState
    val googleCameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(googleMapScreenState.dallasLatLng.latitude, googleMapScreenState.dallasLatLng.longitude), CITY_ZOOM_LEVEL)
    }
    val dialogVisibility = remember {
        mutableStateOf(value = false)
    }

    val fullScreenMaps: State<Boolean> = remember(googleMapScreenState.businessList) {
        derivedStateOf { googleMapScreenState.businessList.value.isEmpty() }
    }

    val showRetryButton = remember {
        mutableStateOf(value = false)
    }

    val clusterMarkers: MutableState<List<Cluster<MapClusterItem>>> = remember {
        mutableStateOf(emptyList())
    }

    val items = remember { mutableStateListOf<MapClusterItem>() }

    LaunchedEffect(key1 = googleMapScreenState.businessList.value, key2 = googleCameraPositionState.isMoving) {
        items.addAll(googleMapScreenState.businessList.value.toMapClusterItems())
        clusterMarkers.value = clusterList(items, algorithm = algorithm, googleCameraPositionState.position.zoom)
    }

    dialogVisibility.value = googleMapScreenState.yelpError.value != UserFacingError.NoError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        if (showRetryButton.value) {
            RetryButton(retry = {
                showRetryButton.value = false
                googleMapScreenState.retrySearch(
                    LatLong(
                        googleCameraPositionState.position.target.latitude,
                        googleCameraPositionState.position.target.longitude
                    ),
                    googleCameraPositionState.position.zoom
                )
            }, modifier = Modifier)
        }

        GoogleMap(
            properties = mapProperties,
            modifier = if (fullScreenMaps.value) Modifier.fillMaxSize() else Modifier
                .height(400.dp)
                .fillMaxWidth(),
            cameraPositionState = googleCameraPositionState
        ) {

            AddClusterMarkers(
                clusterMarkers = clusterMarkers.value, onclick = { marker ->
                    googleMapScreenState.setSelectedMarker(marker.tag as YelpMarker)
                    googleCameraPositionState.move(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                marker.position.latitude,
                                marker.position.longitude
                            )
                        )
                    )
                    true
                },
                googleMapScreenState.yelpMarkerSelected.value,
            )

            if (googleCameraPositionState.isMoving) {
                val search = LatLong(
                    latitude = googleCameraPositionState.position.target.latitude,
                    longitude = googleCameraPositionState.position.target.longitude
                )
                googleMapScreenState.onCameraMoveSearch(search, googleCameraPositionState.position.zoom)
            }
        }

        AnimatedVisibility(dialogVisibility.value) {
            ShowErrorDialog(
                yelpError = googleMapScreenState.yelpError.value,
                onDismiss = {
                    googleMapScreenState.resetError()
                    showRetryButton.value = true
                }
            )
        }

        AnimatedVisibility(googleMapScreenState.businessList.value.isNotEmpty()) {
            YelpBusinessList(
                businessList = googleMapScreenState.businessList.value, onClick = { yelpBusiness ->
                    googleMapScreenState.setSelectedMarker(
                        YelpMarker(
                            latitude = yelpBusiness.coordinates.latitude,
                            longitude = yelpBusiness.coordinates.longitude,
                            businessName = yelpBusiness.businessName
                        )
                    )
                },
                selectedYelpMarker = googleMapScreenState.yelpMarkerSelected.value
            )
        }
    }
}

@Composable
fun ShowErrorDialog(yelpError: UserFacingError, onDismiss: () -> Unit) {
    when (yelpError) {
        is UserFacingError.ApiError -> CustomAlertDialog(
            title = yelpError.title,
            message = yelpError.description,
            onDismiss = onDismiss
        )
        is UserFacingError.GeneralError -> CustomAlertDialog(
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
fun AddMarkers(yelpMarkers: List<YelpMarker>, onclick: (Marker) -> Boolean, yelpMarkerSelected: YelpMarker) {
    yelpMarkers.forEach { yelpMarker ->
        Marker(
            state = MarkerState(
                position = LatLng(yelpMarker.latitude, yelpMarker.longitude)
            ),
            title = yelpMarker.businessName,
            icon = if (yelpMarkerSelected == yelpMarker) {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            } else {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            },
            onClick = onclick,
            tag = yelpMarker,
            zIndex = if (yelpMarkerSelected == yelpMarker) MARKER_TO_FOREGROUND_Z_INDEX else MARKER_TO_BACKGROUND_Z_INDEX
        )
    }
}

@PreviewAllDevices
@Composable
fun GoogleMapPreview() {
    Preview {
        GoogleMapsView(modifier = Modifier.fillMaxSize(), googleMapScreenState = googleMapScreenStateTest)
    }
}
