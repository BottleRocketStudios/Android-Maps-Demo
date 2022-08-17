package com.bottlerocketstudios.mapsdemo.data.implementations

import com.bottlerocketstudios.mapsdemo.data.network.YelpService
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.models.convertToBusiness
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class YelpRepositoryImplementation: YelpRepository, KoinComponent {

    private val yelpService: YelpService by inject()
    private val _businesses = MutableStateFlow<List<Business>>(listOf())

    override val businesses: StateFlow<Business>
        get() = TODO("Not yet implemented")

    override suspend fun getBusinessesByLatLng(yelpLatLngSearch: YelpLatLngSearch) {
       Result.success(yelpService.getBusinessesByLatLng(yelpLatLngSearch.latitude,yelpLatLngSearch.longitude)).onSuccess { result ->
           result.map {
               it.map {businessDTO ->
                   val business = businessDTO.convertToBusiness()
               }
           }

       }.onFailure {

       }
    }

}
