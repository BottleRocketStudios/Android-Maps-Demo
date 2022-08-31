package com.bottlerocketstudios.mapsdemo.domain.repositories

import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.Repository
import com.bottlerocketstudios.mapsdemo.domain.models.LatLong

interface YelpRepository : Repository {

    suspend fun getBusinessesByLatLng(latLong: LatLong, radius: Int?): Result<List<Business>>
}
