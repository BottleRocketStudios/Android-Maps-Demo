package com.bottlerocketstudios.mapsdemo.ui.map

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.YelpLatLngSearch
import com.bottlerocketstudios.mapsdemo.domain.repositories.YelpRepository
import com.bottlerocketstudios.mapsdemo.ui.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class YelpViewModel : BaseViewModel() {
    // DI
    private val yelpRepository: YelpRepository by inject()

    // UI
    private val _yelpBusinessState: MutableStateFlow<List<Business>> = MutableStateFlow(emptyList())
    val yelpBusinessState: StateFlow<List<Business>> = _yelpBusinessState
    val dallasLatLng: LatLng = LatLng(LATITUDE, LONGITUDE)
    private val _yelpErrorState: MutableStateFlow<UserFacingError?> = MutableStateFlow(null)
    val yelpErrorState: StateFlow<UserFacingError?> = _yelpErrorState

    private companion object {
        const val LATITUDE = 32.7767
        const val LONGITUDE = -96.7970
    }

    init {
        getYelpBusinesses(YelpLatLngSearch(dallasLatLng.latitude, dallasLatLng.longitude))
    }

    private fun getYelpBusinesses(yelpLatLngSearch: YelpLatLngSearch) {
        viewModelScope.launch(dispatcherProvider.IO) {
            yelpRepository.getBusinessesByLatLng(yelpLatLngSearch)
                .onSuccess { businessList ->
                    _yelpBusinessState.value = businessList
                }
                .onFailure { throwable ->
                    when (throwable) {
                        is HttpException -> {
                            Timber.d("${throwable.code()} ${ throwable.message() }")
                            _yelpErrorState.value = UserFacingError.NetworkError()
                        }
                        else -> {
                            Timber.d(throwable.localizedMessage)
                            UserFacingError.GeneralError()
                        }
                    }
                    _yelpBusinessState.value = emptyList()
                }
        }
    }
}
