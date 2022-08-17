package com.bottlerocketstudios.mapsdemo.startup

import android.content.Context
import androidx.startup.Initializer
import com.bottlerocketstudios.mapsdemo.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.mapsdemo.data.di.DataModule
import com.bottlerocketstudios.mapsdemo.data.di.NetworkModule
import com.bottlerocketstudios.mapsdemo.data.di.ViewModelModule
import com.bottlerocketstudios.mapsdemo.di.AppModule
import com.bottlerocketstudios.places.di.PlacesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/** AndroidX Startup Koin initializer */
class KoinStartupInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        Timber.v("[create]")
        // Can't use Koin to create this due to necessary logic needed in startKoin for androidLogger. Just create/use an instance here for this special case.
        val buildConfigProvider = BuildConfigProviderImpl()
        return startKoin {
            if (buildConfigProvider.isDebugOrInternalBuild) {
                androidLogger(Level.INFO)
            } else {
                androidLogger(Level.NONE)
            }
            androidContext(context)

            // Turn definition override off (it is on by default) so Koin throws a `DefinitionOverrideException` when a definition is provided in multiple locations of the graph.
            // See https://insert-koin.io/docs/reference/koin-core/modules/#overriding-definition-or-module-310
            allowOverride(override = false)
            modules(
                listOf(
                    AppModule.module,
                    PlacesModule.module,
                    DataModule.module,
                    NetworkModule.module,
                    ViewModelModule.module,
                )
            )
        }
    }

    override fun dependencies(): List<Class<TimberStartupInitializer>> = listOf(TimberStartupInitializer::class.java)
}
