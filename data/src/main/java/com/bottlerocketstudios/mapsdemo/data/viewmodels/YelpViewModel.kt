package com.bottlerocketstudios.mapsdemo.data.viewmodels

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class YelpViewModel: BaseViewModel() {
    private val yelpRepository: YelpRepository by inject()

    fun getYelpBusinesses(yelpLatLngSearch: YelpLatLngSearch) {
        viewModelScope.launch(dispatcherProvider.IO) {
            yelpRepository.getBusinessesByLatLng(yelpLatLngSearch)
                .onSuccess {

                }
                .onFailure {

                }
        }
    }
}
