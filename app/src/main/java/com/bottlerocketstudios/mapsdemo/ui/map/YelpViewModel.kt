package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class YelpViewModel : BaseViewModel() {
    // DI
    private val yelpRepository: YelpRepository by inject()

    // UI
    val yelpBusinessState: MutableStateFlow<List<Business>> = MutableStateFlow(emptyList())
    val googleMapsMarkersLatLng = yelpBusinessState.map { businessList ->
        businessList.map { business ->
            YelpMarker(
                latitude = business.coordinates.latitude,
                longitude = business.coordinates.longitude,
                businessName = business.businessName
            )
        }
    }
    val selectedMarker: MutableStateFlow<YelpMarker> = MutableStateFlow(YelpMarker(latitude = 0.0, longitude = 0.0, businessName = ""))
    val dallasLatLng: LatLong = LatLong(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    private var searchJob: Job? = null

    private companion object {
        const val DEFAULT_LATITUDE = 32.7767
        const val DEFAULT_LONGITUDE = -96.7970
        const val SEARCH_DELAY = 300L
        const val ZOOM_THRESHOLD = 13

        // Yelp Search Radius are in meters. 40,000 meter is about 25 miles
        const val MAX_SEARCH_RADIUS = 40000
    }

    init {
        getYelpBusinesses(LatLong(dallasLatLng.latitude, dallasLatLng.longitude), MAX_SEARCH_RADIUS)
    }

    private fun getYelpBusinesses(latLong: LatLong, radius: Int?) {
        launchIO {
            yelpRepository.getBusinessesByLatLng(latLong, radius)
                .onSuccess { businessList ->
                    yelpBusinessState.value = businessList
                }.handleFailure()
        }
    }

    fun getYelpBusinessesOnMapMove(latLong: LatLong, zoomLevel: Float) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch(dispatcherProvider.IO) {
            // Delay is added to wait for the camera to stop moving before performing
            // the search.
            delay(SEARCH_DELAY)

            getYelpBusinesses(
                latLong = latLong,
                radius = if (zoomLevel > ZOOM_THRESHOLD) {
                    null
                } else {
                    MAX_SEARCH_RADIUS
                }
            )
        }
    }

    fun resetError() {
        errorStateFlow.value = UserFacingError.NoError
    }
    fun retrySearch() {
        getYelpBusinesses(LatLong(dallasLatLng.latitude, dallasLatLng.longitude), radius = null)
    }

    fun setSelectedMarker(yelpMarker: YelpMarker) {
        selectedMarker.value = yelpMarker
    }
}
