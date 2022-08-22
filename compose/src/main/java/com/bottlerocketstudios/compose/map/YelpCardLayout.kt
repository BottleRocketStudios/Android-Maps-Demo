package com.bottlerocketstudios.compose.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.utils.Preview
import com.bottlerocketstudios.launchpad.compose.bold

import com.bottlerocketstudios.mapsdemo.domain.models.Business

@Composable
fun YelpCardLayout(business: Business, selectItem: (business: Business) -> Unit) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .clickable {
                selectItem(business)
            }
            .background(color = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_1,
                    end = Dimens.grid_1,
                    top = Dimens.grid_1,
                    bottom = Dimens.grid_1
                )
        ) {
            AsyncImage(model = business.imageUrl, contentDescription = null, modifier = Modifier.height(100.dp).width(100.dp).background(Color.Black))

            BusinessDescriptionComponent(business = business, modifier = Modifier)
        }
    }
}

@Composable
fun BusinessDescriptionComponent(business: Business, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = business.businessName,
            style = typography.h1.bold(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1,
                    start = Dimens.grid_1
                )
        )
        Text(
            text = stringResource(
                id = R.string.latitude_longitude_format,
                business.coordinates.latitude.toString(), business.coordinates.longitude.toString()
            ),
            style = typography.body1,
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5,
                    start = Dimens.grid_1
                ).fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YelpCardPreview() {
    Preview {
        YelpCardLayout(business = yelpTestCard, selectItem = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ShowBusinessDescriptionComponent() {
    Preview {
        BusinessDescriptionComponent(business = yelpTestCard, modifier = Modifier)
    }
}
