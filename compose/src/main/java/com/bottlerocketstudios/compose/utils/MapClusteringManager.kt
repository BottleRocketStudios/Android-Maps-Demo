package com.bottlerocketstudios.compose.utils

import com.bottlerocketstudios.mapsdemo.domain.infrastructure.coroutine.DispatcherProvider
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.Algorithm
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithm
import com.google.maps.android.clustering.algo.ScreenBasedAlgorithmAdapter
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MapClusteringManager<T : ClusterItem>(googleCameraPositionState: CameraPositionState, markerClicked: () -> Unit): KoinComponent {
    private val dispatcherProvider: DispatcherProvider by inject()
    private var searchJob: Job? = null

    private val cameraPositionState: CameraPositionState = googleCameraPositionState

    private var algorithm: ScreenBasedAlgorithm<T> = ScreenBasedAlgorithmAdapter(
        PreCachingAlgorithmDecorator(
            NonHierarchicalDistanceBasedAlgorithm()
        )
    )

    fun setAlgorithm(passedInAlgorithm: ScreenBasedAlgorithm<T>) {
        algorithm.lock()
        try {
            val oldAlgorithm = getAlgorithm()
            algorithm = passedInAlgorithm
            oldAlgorithm.lock()
            try {
                algorithm.addItems(oldAlgorithm.items)
            } finally {
                oldAlgorithm.unlock()
            }
        } finally {
            algorithm.unlock()
        }

        if (algorithm.shouldReclusterOnMapMovement()) {
            algorithm.onCameraChange(cameraPositionState.position)
        }

    }

    private fun getAlgorithm(): Algorithm<T> = algorithm

    /**
     *  This should be called when adding, removing, updating clearing item(s).
     */

    fun cluster() {
        searchJob?.cancel()
        searchJob = CoroutineScope(dispatcherProvider.IO).launch {
            clusterTask()
        }

    }

    fun setRenderer(renderer: ClusterRenderer<T>) {

    }

    private  fun clusterTask(): MutableSet<out Cluster<T>>? {
        val algorithm: Algorithm<T> = getAlgorithm()
        algorithm.lock()
        try {
            return algorithm.getClusters(cameraPositionState.position.zoom)
        } finally {
            algorithm.unlock()
        }
    }

    fun onCameraIdle() {

    }


}
