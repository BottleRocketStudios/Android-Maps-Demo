package com.bottlerocketstudios.compose.yelp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bottlerocketstudios.compose.map.YelpCardLayout
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.mapsdemo.domain.models.Business

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.YelpBusinessList(businessList: List<Business>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
        modifier = Modifier
            .padding(
                start = Dimens.grid_1_5,
                end = Dimens.grid_1_5,
                top = Dimens.grid_1_5,
                bottom = Dimens.grid_1_5
            )
            .fillMaxWidth()
            .weight(1f)
    ) {
        items(
            items = businessList,
            itemContent = { item ->
                YelpCardLayout(
                    business = item,
                    selectItem = {},
                    modifier = Modifier.animateItemPlacement()
                )
            }
        )
    }
}
