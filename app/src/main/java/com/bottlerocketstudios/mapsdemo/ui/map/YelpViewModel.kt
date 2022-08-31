package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
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
        const val SEARCH_DELAY_MILLS = 300L
        const val ZOOM_THRESHOLD = 13f

        // Yelp Search Radius are in meters. 40,000 meter is about 25 miles
        const val MAX_SEARCH_RADIUS_METERS = 40000
    }

    init {
        getYelpBusinesses(LatLong(dallasLatLng.latitude, dallasLatLng.longitude), MAX_SEARCH_RADIUS_METERS)
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
            delay(SEARCH_DELAY_MILLS)

            getYelpBusinesses(
                latLong = latLong,
                radius = determineRadius(zoomLevel)
            )
        }
    }

    fun resetError() {
        errorStateFlow.value = UserFacingError.NoError
    }
    fun retrySearch(latLong: LatLong, zoomLevel: Float) {
        getYelpBusinesses(latLong, radius = determineRadius(zoomLevel))
    }

    fun setSelectedMarker(yelpMarker: YelpMarker) {
        selectedMarker.value = yelpMarker
    }

    private fun determineRadius(zoomLevel: Float): Int? {
        return if (zoomLevel > ZOOM_THRESHOLD) {
            null
        } else {
            MAX_SEARCH_RADIUS_METERS
        }
    }
}
