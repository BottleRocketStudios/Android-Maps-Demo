package com.bottlerocketstudios.mapsdemo.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.domain.infrastructure.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel: ViewModel(), KoinComponent {
    // DI
    protected val dispatcherProvider: DispatcherProvider by inject()

    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(dispatcherProvider.IO, block = block)

    /**
     * Utility function to switch coroutine context to Main.
     * Useful for making UI updates from IO
     */
    suspend fun runOnMain(block: suspend CoroutineScope.() -> Unit) =
        withContext(dispatcherProvider.Main, block)

}
