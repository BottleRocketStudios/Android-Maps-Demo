package com.bottlerocketstudios.compose.utils.map

import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

fun mapClustering(yelpMarkers: List<YelpMarker>): List<MapClusterItem> {
    val items = mutableListOf<MapClusterItem>()

    yelpMarkers.forEach { yelpMarkers ->
        val latLng = LatLng(
            yelpMarkers.latitude,
            yelpMarkers.longitude
        )
        items.add(MapClusterItem(latLng, yelpMarkers.businessName, yelpMarkers.businessName))
    }
    return items
}

data class MapClusterItem(
    val itemLatLng: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemLatLng

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet
}
