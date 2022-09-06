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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.alertdialog.CustomAlertDialog
import com.bottlerocketstudios.compose.utils.Preview
import com.bottlerocketstudios.compose.utils.PreviewAllDevices
import com.bottlerocketstudios.compose.utils.map.MapClusterItem
import com.bottlerocketstudios.compose.utils.map.clusteringIconGenerator
import com.bottlerocketstudios.compose.utils.map.toMapClusterItems
import com.bottlerocketstudios.compose.yelp.RetryButton
import com.bottlerocketstudios.compose.yelp.YelpBusinessList
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithm
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithmAdapter
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber

// https://developers.google.com/maps/documentation/android-sdk/views#zoom
private const val MAX_ZOOM_LEVEL = 15f // Street level
private const val MIN_ZOOM_LEVEL = 5f // Landmass/Continent
private const val CITY_ZOOM_LEVEL = 11f
private const val MARKER_TO_FOREGROUND_Z_INDEX = 100f
private const val MARKER_TO_BACKGROUND_Z_INDEX = 0f

private var algorithm: ScreenBasedAlgorithm<MapClusterItem> = ScreenBasedAlgorithmAdapter(
    PreCachingAlgorithmDecorator(
        NonHierarchicalDistanceBasedAlgorithm()
    )
)

fun clusterList(clusterItems: List<MapClusterItem>, algorithm: ScreenBasedAlgorithm<MapClusterItem>, zoom: Float): List<Cluster<MapClusterItem>> {
    algorithm.lock()
    try {
        val oldAlgorithm = algorithm

        oldAlgorithm.lock()
        try {
            algorithm.addItems(oldAlgorithm.items)
        } finally {
            oldAlgorithm.unlock()
        }
        algorithm.addItems(clusterItems)
        return algorithm.getClusters(zoom).toList()
    } finally {
        algorithm.unlock()
    }
}

@Composable
fun GoogleMapsView(googleMapScreenState: GoogleMapScreenState, toolbarEnabled: Boolean = false, modifier: Modifier) {
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
            })
        }

        GoogleMap(
            properties = mapProperties,
            modifier = if (fullScreenMaps.value) Modifier.fillMaxSize() else Modifier
                .height(400.dp)
                .fillMaxWidth(),
            cameraPositionState = googleCameraPositionState
        ) {

            AddClusterMarkers(clusterMarkers = clusterMarkers.value, onclick = { marker ->
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
            }, googleMapScreenState.yelpMarkerSelected.value)


                /*AddMarkers(
                    yelpMarkers = googleMapScreenState.googleMarkers.value,
                    onclick = { marker ->
                        Timber.d("${marker.id} ${marker.title}")
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
                    googleMapScreenState.yelpMarkerSelected.value
                )*/

            // }

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
fun AddMarkers(yelpMarker: YelpMarker, onclick: (Marker) -> Boolean, yelpMarkerSelected: YelpMarker) {

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

@Composable
fun AddClusterMarkers(clusterMarkers: List<Cluster<MapClusterItem>>, onclick: (Marker) -> Boolean, yelpMarkerSelected: YelpMarker) {

    clusterMarkers.forEach { cluster ->
        
        if (cluster.size <= 2) {
            cluster.items.forEach { mapClusterItem ->
                val yelpMarker = YelpMarker(mapClusterItem.itemLatLng.latitude, mapClusterItem.itemLatLng.longitude, mapClusterItem.itemTitle)
                AddMarkers(
                    yelpMarker = yelpMarker,
                    onclick = onclick,
                    yelpMarkerSelected = yelpMarkerSelected
                )
            }
        } else {
            Marker(
                state = MarkerState(
                    position = LatLng(cluster.position.latitude, cluster.position.longitude),
                ),
                title = cluster.items.size.toString(),
                icon = clusteringIconGenerator(context = LocalContext.current, cluster)
            )
        }
    }
}

@PreviewAllDevices
@Composable
fun GoogleMapPreview() {
    Preview {
        GoogleMapsView(modifier = Modifier.fillMaxSize(), googleMapScreenState = googleMapScreenStateTest)
    }
}
