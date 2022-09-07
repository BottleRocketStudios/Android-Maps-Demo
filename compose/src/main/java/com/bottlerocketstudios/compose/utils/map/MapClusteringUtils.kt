package com.bottlerocketstudios.compose.utils.map

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithm

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
