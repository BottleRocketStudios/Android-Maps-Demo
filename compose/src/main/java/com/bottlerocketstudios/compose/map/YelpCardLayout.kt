package com.bottlerocketstudios.compose.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.utils.Preview

import com.bottlerocketstudios.mapsdemo.domain.models.Business

@Composable
fun YelpCardLayout(business: Business, selectItem: (business: Business) -> Unit) {
    Card(
        elevation = 2.dp,
        modifier = Modifier.clickable {
            selectItem(business)
        }
    ){
        Row(
            modifier = Modifier.padding(
                start = 6.dp,
                end = 6.dp,
                top = 6.dp,
                bottom = 6.dp
            )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.wrapContentWidth()
            )
        }
    } 
}

@Preview(showSystemUi = true)
@Composable
fun YelpCardLPreview() {
    Preview {
        YelpCardLayout(business = yelpTestCard)
    }
}
