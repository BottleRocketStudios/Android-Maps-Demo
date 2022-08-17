package com.bottlerocketstudios.mapsdemo.data.implementations

import com.bottlerocketstudios.mapsdemo.data.network.YelpService
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.data.model.convertToBusiness
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class YelpRepositoryImplementation: YelpRepository, KoinComponent {
    // DI
    private val yelpService: YelpService by inject()

    override suspend fun getBusinessesByLatLng(yelpLatLngSearch: YelpLatLngSearch): Result<List<Business>> =
        yelpService.getBusinessesByLatLng(yelpLatLngSearch.latitude,yelpLatLngSearch.longitude).map { businessList ->
            businessList.map {
                it.convertToBusiness()
            }
        }

}
