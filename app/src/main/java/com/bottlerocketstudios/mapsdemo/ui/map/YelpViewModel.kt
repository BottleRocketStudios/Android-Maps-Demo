package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
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
