package com.bottlerocketstudios.mapsdemo.data.network

import com.bottlerocketstudios.mapsdemo.domain.models.BusinessDTO
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface YelpService {
    /** https://api.yelp.com/v3/businesses/search **/
    @GET(value = "businesses/search")
    suspend fun getBusinessesByLatLng(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Result<List<BusinessDTO>>
}
