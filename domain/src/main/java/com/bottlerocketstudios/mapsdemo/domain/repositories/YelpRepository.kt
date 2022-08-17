package com.bottlerocketstudios.mapsdemo.domain.repositories

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.Repository
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import kotlinx.coroutines.flow.StateFlow

interface YelpRepository: Repository {
    val businesses: StateFlow<Business>

    suspend fun getBusinessesByLatLng(yelpLatLngSearch: YelpLatLngSearch)
}
