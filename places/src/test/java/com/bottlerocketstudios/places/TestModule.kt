package com.bottlerocketstudios.places

import com.bottlerocketstudios.places.mocks.testContext
import com.google.android.libraries.places.api.Places
import org.koin.dsl.module

object TestModule {

    fun generateMockedTestModule() = module {
        single { testContext }
        single { Places.createClient(get()) }
    }
}
