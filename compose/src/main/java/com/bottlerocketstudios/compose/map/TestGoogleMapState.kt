package com.bottlerocketstudios.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.bottlerocketstudios.compose.utils.asMutableState

val googleMapScreenStateTest = GoogleMapScreenState ( businessList =
    mutableStateOf(listOf(yelpTestCard))
    )
