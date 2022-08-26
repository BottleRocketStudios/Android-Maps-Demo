package com.bottlerocketstudios.places.startup

import androidx.startup.Initializer

/** Left here as a breadcrumb that a startup initializer does exist. The actual implementation is in the app module due the dependency it has on KoinStartupInitializer (also in the app module) */
interface BasePlacesStartupInitializer : Initializer<Unit>
