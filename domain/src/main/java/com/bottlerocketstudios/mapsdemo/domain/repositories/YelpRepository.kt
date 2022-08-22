package com.bottlerocketstudios.mapsdemo.domain.repositories

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.Repository
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch

interface YelpRepository : Repository {

    suspend fun getBusinessesByLatLng(yelpLatLngSearch: YelpLatLngSearch): Result<List<Business>>
}
