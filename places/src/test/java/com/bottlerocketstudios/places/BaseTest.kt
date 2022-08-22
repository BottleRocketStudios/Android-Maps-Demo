package com.bottlerocketstudios.places

import org.junit.Before
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import timber.log.Timber

open class BaseTest {
    @Before
    fun plantTimber() {
        Timber.plant(SystemOutPrintlnTree())
    }

    inline fun <reified T> inlineKoinSingle(crossinline block: org.koin.core.scope.Scope.() -> T) {
        loadKoinModules(module { single { block() } })
    }
}
