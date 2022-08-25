package com.bottlerocketstudios.mapsdemo.data.network

import com.bottlerocketstudios.mapsdemo.data.model.YelpBusinessesDTO
import retrofit2.http.GET
import retrofit2.http.Query

internal interface YelpService {
    /** https://api.yelp.com/v3/businesses/search **/
    @GET(value = "businesses/search")
    suspend fun getBusinessesByLatLng(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("limit") limit: Int,
        @Query("radius") radius: Int?,
    ): Result<YelpBusinessesDTO>
}
