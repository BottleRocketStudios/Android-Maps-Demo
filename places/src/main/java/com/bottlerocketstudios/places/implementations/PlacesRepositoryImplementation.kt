package com.bottlerocketstudios.places.implementations

import com.bottlerocketstudios.mapsdemo.domain.models.PlaceSearchEventIdle
import com.bottlerocketstudios.mapsdemo.domain.models.PlacesSearchEvent
import com.bottlerocketstudios.mapsdemo.domain.repositories.PlacesRepository
import com.google.android.libraries.places.api.net.PlacesClient
import org.koin.core.component.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent



internal class PlacesRepositoryImplementation() : PlacesRepository, KoinComponent {
    private val placesClient: PlacesClient by inject()

    private val _placesAutocompletePredictionEvents = MutableStateFlow<PlacesSearchEvent>(PlaceSearchEventIdle)
    private val _placeDetails = MutableStateFlow<PlacesSearchEvent>(PlaceSearchEventIdle)

    override val placesEvents: StateFlow<PlacesSearchEvent> = _placesAutocompletePredictionEvents
    override val placeDetails: StateFlow<PlacesSearchEvent> = _placeDetails


    // To be used later when we implement places
    /*override suspend fun getPlaces(placesRequest: PlacesRequest) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            _placesAutocompletePredictionEvents.value = PlacesSearchEventError(throwable)
        }

        val scope = CoroutineScope(SupervisorJob() + handler)

        scope.launch {
            val request = findAutocompletePredictionsRequest {
                locationBias = placesRequest.bias
                typeFilter = placesRequest.typeFilter
                query = placesRequest.query
                countries = placesRequest.countries
            }

            val response = placesClient.awaitFindAutocompletePredictions(request)
            //_placesAutocompletePredictionEvents.value = PlacesSearchEventFound(response.autocompletePredictions)
        }
    }*/

    // To be used later when we need to implement places auto predictions
    /*override suspend fun onAutoCompletePredictionSelected(prediction: AutocompletePrediction) {

        // For all the different Fields you can use see https://developers.google.com/maps/documentation/places/web-service/place-data-fields
        val placeRequest = fetchPlaceRequest(
            prediction.placeId,
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.BUSINESS_STATUS
            )
        )

        val placeResponse = placesClient.awaitFetchPlace(request = placeRequest)

        _placeDetails.value = PlaceDetailsFound(placeResponse.place)
    }*/
}
