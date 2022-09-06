package com.bottlerocketstudios.compose.utils.map

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

fun List<Business>.toMapClusterItems(): List<MapClusterItem> {
    val items = mutableListOf<MapClusterItem>()

    this.forEach { business ->
        val latLng = LatLng(
            business.coordinates.latitude,
            business.coordinates.longitude
        )
        items.add(MapClusterItem(latLng, business.businessName, business.businessName))
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
