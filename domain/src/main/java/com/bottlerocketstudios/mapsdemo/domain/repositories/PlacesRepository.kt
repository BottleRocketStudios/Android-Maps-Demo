package com.bottlerocketstudios.mapsdemo.domain.repositories

import com.bottlerocketstudios.mapsdemo.domain.models.PlacesSearchEvent
import com.bottlerocketstudios.mapsdemo.domain.models.Repository
import kotlinx.coroutines.flow.StateFlow

interface PlacesRepository : Repository {
    val placesEvents: StateFlow<PlacesSearchEvent>
    val placeDetails: StateFlow<PlacesSearchEvent>
    // suspend fun getPlaces(placesRequest: PlacesRequest)
    // To be added later when we need to use auto predictions in the app
    // suspend fun onAutoCompletePredictionSelected()
}
