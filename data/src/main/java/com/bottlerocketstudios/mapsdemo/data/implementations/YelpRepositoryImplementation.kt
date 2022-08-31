package com.bottlerocketstudios.mapsdemo.data.implementations

import com.bottlerocketstudios.mapsdemo.data.model.YelpBusinessesDto
import com.bottlerocketstudios.mapsdemo.data.network.YelpService
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong
import com.bottlerocketstudios.mapsdemo.data.model.convertToBusiness
import com.bottlerocketstudios.mapsdemo.data.utils.mapErrors
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class YelpRepositoryImplementation : YelpRepository, KoinComponent {
    // DI
    private val yelpService: YelpService by inject()

    override suspend fun getBusinessesByLatLng(latLong: LatLong, radiusMeters: Int?): Result<List<Business>> =
        yelpService.getBusinessesByLatLng(latLong.latitude, latLong.longitude, radiusMeters = radiusMeters).map { yelpSearch: YelpBusinessesDto ->
            yelpSearch.businesses.map { businessDto -> businessDto.convertToBusiness() }
        }.mapErrors()
}
