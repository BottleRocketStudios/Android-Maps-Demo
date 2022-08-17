package com.bottlerocketstudios.mapsdemo.buildconfig

import com.bottlerocketstudios.mapsdemo.BuildConfig
import com.bottlerocketstudios.mapsdemo.domain.di.BuildConfigProvider

class BuildConfigProviderImpl : BuildConfigProvider {
    override val isDebugOrInternalBuild: Boolean
        get() = isDebugOrInternalBuild()
    override val isProductionReleaseBuild: Boolean
        get() = isProductionReleaseBuild()
    override val buildIdentifier: String
        get() = BuildConfig.BUILD_IDENTIFIER
    override val mapsApiKey: String
        get() = BuildConfig.MAPS_API_KEY
    override val yelpApiKey: String
        get() = BuildConfig.YELP_API_KEY
}

private fun isProductionReleaseBuild() = !BuildConfig.DEBUG && BuildConfig.PRODUCTION

private fun isDebugOrInternalBuild() = BuildConfig.DEBUG || BuildConfig.INTERNAL
