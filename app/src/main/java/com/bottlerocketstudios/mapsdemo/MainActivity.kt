package com.bottlerocketstudios.mapsdemo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.map.GoogleMapsView
import com.bottlerocketstudios.compose.map.googleMapScreenStateTest
import com.bottlerocketstudios.mapsdemo.ui.map.YelpViewModel
import com.bottlerocketstudios.mapsdemo.ui.map.toState
import com.bottlerocketstudios.mapsdemo.ui.theme.AndroidMapsDemoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val yelpViewModel: YelpViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidMapsDemoTheme {
                GoogleMapsView(toolbarEnabled = false, modifier = Modifier.fillMaxSize(), googleMapScreenState = yelpViewModel.toState())
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidMapsDemoTheme {
        GoogleMapsView(
            toolbarEnabled = false,
            modifier = Modifier.fillMaxSize(),
            googleMapScreenState = googleMapScreenStateTest
        )
    }
}


