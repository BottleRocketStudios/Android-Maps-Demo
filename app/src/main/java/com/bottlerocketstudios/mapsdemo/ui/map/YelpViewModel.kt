package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class YelpViewModel : BaseViewModel() {
    // DI
    private val yelpRepository: YelpRepository by inject()

    // UI
    val yelpBusinessState: MutableStateFlow<List<Business>> = MutableStateFlow(emptyList())
    val dallasLatLng: LatLng = LatLng(LATITUDE, LONGITUDE)
    // move to baseviewmodel

    private companion object {
        const val LATITUDE = 32.7767
        const val LONGITUDE = -96.7970
    }

    init {
        getYelpBusinesses(YelpLatLngSearch(dallasLatLng.latitude, dallasLatLng.longitude))
    }

    private fun getYelpBusinesses(yelpLatLngSearch: YelpLatLngSearch) {
        viewModelScope.launch(dispatcherProvider.IO) {
            yelpRepository.getBusinessesByLatLng(yelpLatLngSearch)
                .onSuccess { businessList ->
                    yelpBusinessState.value = businessList
                }.handleFailure()

        }
    }
}
