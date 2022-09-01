package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

@Composable
fun MapClustering(yelpMarkers: List<YelpMarker>): List<MapClusterItem> {
    val items = remember { mutableStateListOf<MapClusterItem>() }
    LaunchedEffect(Unit) {
        yelpMarkers.forEach { yelpMarkers ->
            val latLng = LatLng(
                yelpMarkers.latitude,
                yelpMarkers.longitude
            )
            items.add(MapClusterItem(latLng, yelpMarkers.businessName, yelpMarkers.businessName))
        }
    }
    return items
}



data class MapClusterItem(
    val itemLatLng: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
) : ClusterItem {
    override fun getPosition(): LatLng  =
        itemLatLng

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet
}
