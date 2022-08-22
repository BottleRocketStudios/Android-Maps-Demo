package com.bottlerocketstudios.places

import org.koin.core.module.Module
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class KoinTestRule(private val module: Module) : TestWatcher() {

    override fun starting(description: Description?) {
        startKoin {
            modules(module)
        }
    }

    override fun finished(description: Description?) = stopKoin()
}
