package com.bottlerocketstudios.compose.map

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottlerocketstudios.compose.utils.Preview

import com.bottlerocketstudios.mapsdemo.domain.models.Business

@Composable
fun YelpCardLayout(business: Business) {
    Card(elevation = 2.dp){
        Row() {
            
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
