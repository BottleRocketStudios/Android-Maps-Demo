package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class YelpViewModel: BaseViewModel() {
    // DI
    private val yelpRepository: YelpRepository by inject()

    // UI
    private val _yelpBusinessState = MutableStateFlow<List<Business>>(emptyList())
    val yelpBusinessState: StateFlow<List<Business>> = _yelpBusinessState
    val dallasLatLng: LatLng = LatLng(32.7767,-96.7970)

    init {
        getYelpBusinesses(YelpLatLngSearch(dallasLatLng.latitude, dallasLatLng.longitude))
    }

    fun getYelpBusinesses(yelpLatLngSearch: YelpLatLngSearch) {
        viewModelScope.launch(dispatcherProvider.IO) {
            yelpRepository.getBusinessesByLatLng(yelpLatLngSearch)
                .onSuccess {businessList ->
                    _yelpBusinessState.value = businessList
                }
                .onFailure {
                    _yelpBusinessState.value = emptyList()
                }
        }
    }
}
