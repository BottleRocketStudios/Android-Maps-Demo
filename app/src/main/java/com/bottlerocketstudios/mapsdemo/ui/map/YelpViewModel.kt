package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class YelpViewModel : BaseViewModel() {
    // DI
    private val yelpRepository: YelpRepository by inject()

    // UI
    val yelpBusinessState: MutableStateFlow<List<Business>> = MutableStateFlow(emptyList())
    val googleMapsMarkersLatLng: MutableStateFlow<List<YelpMarker>> = MutableStateFlow(emptyList())
    val dallasLatLng: LatLng = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    private var searchJob: Job? = null

    private companion object {
        const val DEFAULT_LATITUDE = 32.7767
        const val DEFAULT_LONGITUDE = -96.7970
        const val SEARCH_DELAY = 300L
    }

    init {
        getYelpBusinesses(YelpLatLngSearch(dallasLatLng.latitude, dallasLatLng.longitude))
    }

    private fun getYelpBusinesses(yelpLatLngSearch: YelpLatLngSearch) {
        viewModelScope.launch(dispatcherProvider.IO) {
            yelpRepository.getBusinessesByLatLng(yelpLatLngSearch)
                .onSuccess { businessList ->
                    yelpBusinessState.value = businessList
                    googleMapsMarkersLatLng.value = businessList.map { business ->
                        YelpMarker(
                            latitude = business.coordinates.latitude,
                            longitude = business.coordinates.longitude,
                            businessName = business.businessName)
                    }

                }.handleFailure()
        }
    }

    fun getYelpBusinessesOnMapMove(yelpLatLngSearch: YelpLatLngSearch) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch(dispatcherProvider.IO) {
            // Delay is added to wait for the camera to stop moving before performing
            // the search.
            delay(SEARCH_DELAY)

            getYelpBusinesses(yelpLatLngSearch)
        }
    }

    fun resetError() {
        errorStateFlow.value = UserFacingError.NoError
    }
    fun retrySearch() {
        getYelpBusinesses(YelpLatLngSearch(dallasLatLng.latitude, dallasLatLng.longitude))
    }
}
