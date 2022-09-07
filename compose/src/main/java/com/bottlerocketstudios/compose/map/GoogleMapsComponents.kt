package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bottlerocketstudios.compose.utils.map.MapClusterItem
import com.bottlerocketstudios.compose.utils.map.clusterIconGenerator
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

private const val MARKER_TO_FOREGROUND_Z_INDEX = 100f
private const val MARKER_TO_BACKGROUND_Z_INDEX = 0f
private const val MIN_CLUSTER_SIZE = 3

@Composable
@GoogleMapComposable
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
@GoogleMapComposable
fun AddClusterMarkers(clusterMarkers: List<Cluster<MapClusterItem>>, onclick: (Marker) -> Boolean, yelpMarkerSelected: YelpMarker) {

    clusterMarkers.forEach { cluster ->
        if (cluster.size < MIN_CLUSTER_SIZE) {
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
                icon = clusterIconGenerator(context = LocalContext.current, cluster)
            )
        }
    }
}
