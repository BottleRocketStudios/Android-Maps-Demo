package com.bottlerocketstudios.mapsdemo.data.network

import com.bottlerocketstudios.mapsdemo.data.model.YelpBusinessesDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 ** https://api.yelp.com/v3/businesses/search
 *
 * For additional documentation
 * https://www.yelp.com/developers/documentation/v3/business_search
 *
 *
 * **/

private const val MAX_RADIUS = 50

internal interface YelpService {
    @GET(value = "businesses/search")
    suspend fun getBusinessesByLatLng(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        // Optional. Number of business results to return. By default, it will return 20. Maximum is 50.
        @Query("limit") limit: Int = MAX_RADIUS,
        // Optional. A suggested search radius in meters. This field is used as a suggestion to the search.
        // The actual search radius may be lower than the suggested radius in dense urban areas, and higher in regions of less business
        // density.
        @Query("radius") radiusMeters: Int?,
    ): Result<YelpBusinessesDto>
}
