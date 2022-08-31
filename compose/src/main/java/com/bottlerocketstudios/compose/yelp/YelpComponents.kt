package com.bottlerocketstudios.compose.yelp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.map.YelpCardLayout
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.mapsdemo.domain.models.Business
import com.bottlerocketstudios.mapsdemo.domain.models.YelpMarker
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.YelpBusinessList(businessList: List<Business>, selectedYelpMarker: YelpMarker, onClick: (Business) -> Unit) {

    val noPosition = -1
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // a noPosition is to prevent it from rubber banding  back to the item position if you try to scroll the lazy column
    val itemPosition = remember {
        mutableStateOf(value = noPosition)
    }

    coroutineScope.launch {
        businessList.forEachIndexed { index, business ->
            if (business.businessName == selectedYelpMarker.businessName) {
                itemPosition.value = index
            }
        }
    }

    LazyColumn(
        state = state,
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
        items(businessList, key = { business -> business.id }) { item ->
            YelpCardLayout(
                business = item,
                selectItem = { onClick(item) },
                modifier = Modifier.animateItemPlacement(),
                isSelected = selectedYelpMarker.businessName == item.businessName
            )
            if (item.businessName == selectedYelpMarker.businessName) {
                Timber.d("${item.businessName} - ${selectedYelpMarker.businessName}")
            }
            if (itemPosition.value > noPosition) {
                coroutineScope.launch {
                    // Scroll to item then a noPosition to prevent it from rubber banding back to the item
                    state.animateScrollToItem(itemPosition.value)
                    itemPosition.value = noPosition
                }
            }
        }
    }
}

@Composable
fun ColumnScope.RetryButton(retry: () -> Unit, modifier: Modifier = Modifier.align(Alignment.CenterHorizontally)) {
    Button(onClick = retry, modifier = modifier.padding(Dimens.grid_1)) {
        Text(text = stringResource(id = R.string.retry))
    }
}
