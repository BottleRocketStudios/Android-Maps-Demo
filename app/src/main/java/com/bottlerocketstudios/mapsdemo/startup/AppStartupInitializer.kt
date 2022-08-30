package com.bottlerocketstudios.mapsdemo.startup

import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

class AppStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.v("[create]")
        // no-op
    }

    override fun dependencies(): List<Class<out Initializer<out Any>>> = listOf(
        TimberStartupInitializer::class.java,
        KoinStartupInitializer::class.java,
        PlacesStartupInitializer::class.java
    )
}
