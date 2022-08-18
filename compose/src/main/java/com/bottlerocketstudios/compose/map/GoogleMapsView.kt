package com.bottlerocketstudios.compose.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.utils.Preview
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

// https://developers.google.com/maps/documentation/android-sdk/views#zoom
private const val MAX_ZOOM_LEVEL = 15f // Street level
private const val MIN_ZOOM_LEVEL = 5f // Landmass/Continent

@Composable
fun GoogleMapsView(googleMapScreenState: GoogleMapScreenState, toolbarEnabled: Boolean = false, modifier: Modifier) {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = MAX_ZOOM_LEVEL, minZoomPreference = MIN_ZOOM_LEVEL)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(MapUiSettings(mapToolbarEnabled = toolbarEnabled))
    }

    Box(modifier.fillMaxSize()) {
        Column {
            GoogleMap(properties = mapProperties, uiSettings = mapUiSettings)
            if(googleMapScreenState.businessList.value.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
                    modifier = Modifier
                        .padding(
                            start = Dimens.grid_1_5,
                            end = Dimens.grid_1_5
                        )
                        .fillMaxWidth()
                ) {
                    items(items = googleMapScreenState.businessList.value,
                    itemContent = {item -> YelpCardLayout(business = item, selectItem = {}) })
                }
            }
        }
        Column {
            Button(onClick = {
                mapUiSettings = mapUiSettings.copy(
                    mapToolbarEnabled = !mapUiSettings.mapToolbarEnabled
                )
            }) {
                Text(text = stringResource(R.string.toggle_map_toolbar))
            }
        }
    }
}

@Preview
@Composable
fun GoogleMapPreview() {
    Preview {
        GoogleMap(modifier = Modifier.fillMaxSize())
    }
}
