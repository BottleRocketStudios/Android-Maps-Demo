package com.bottlerocketstudios.compose.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
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
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_1,
                    end = Dimens.grid_1,
                    top = Dimens.grid_1,
                    bottom = Dimens.grid_1
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.wrapContentWidth()
            ) {
                AsyncImage(model = business.imageUrl, contentDescription = null )
            }
            Row(
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.Top)
            ) {
                BusinessDescriptionComponent(business = business, modifier = Modifier.weight(2f) )
            }
        }
    } 
}

@Composable
fun BusinessDescriptionComponent(business: Business, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = business.businessName,
            style = MaterialTheme.typography.h3.bold(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(id = R.string.latitude_longitude_format,
                business.coordinates.latitude.toString(), business.coordinates.longitude.toString()),
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun YelpCardPreview() {
    Preview {
        YelpCardLayout(business = yelpTestCard, selectItem ={})
    }
}
