package com.bottlerocketstudios.compose.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.utils.Preview
import com.bottlerocketstudios.mapsdemo.domain.models.Business
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

        Column(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                properties = mapProperties,
                uiSettings = mapUiSettings,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
            )

            if(googleMapScreenState.businessList.value.isNotEmpty()) {
                YelpBusinessList(businessList = googleMapScreenState.businessList.value)
            }
            Button(onClick = {
                mapUiSettings = mapUiSettings.copy(
                    mapToolbarEnabled = !mapUiSettings.mapToolbarEnabled
                )
            }) {
                Text(text = stringResource(R.string.toggle_map_toolbar))
            }

        }

}

@Composable
fun ColumnScope.YelpBusinessList(businessList: List<Business>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
        modifier = Modifier
            .padding(
                start = Dimens.grid_1_5,
                end = Dimens.grid_1_5
            )
            .fillMaxWidth()
            .weight(1f)
    ) {
        items(items = businessList,
            itemContent = {item -> YelpCardLayout(business = item, selectItem = {}) })
    }
}

@Preview
@Composable
fun GoogleMapPreview() {
    Preview {
        GoogleMapsView(modifier = Modifier.fillMaxSize(), googleMapScreenState = googleMapScreenStateTest)
    }
}

@Preview
@Composable
fun YelpBusinessListPreview() {
    Preview {
        //YelpBusinessList(businessList = listOf(yelpTestCard))
    }
}
