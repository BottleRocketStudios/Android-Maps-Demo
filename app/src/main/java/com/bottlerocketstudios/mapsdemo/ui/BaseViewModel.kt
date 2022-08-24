package com.bottlerocketstudios.mapsdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.mapsdemo.R
import com.bottlerocketstudios.mapsdemo.domain.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.mapsdemo.domain.models.UserFacingError
import com.bottlerocketstudios.mapsdemo.domain.models.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.io.IOException

abstract class BaseViewModel : ViewModel(), KoinComponent {
    // DI
    protected val dispatcherProvider: DispatcherProvider by inject()

    val errorStateFlow: MutableStateFlow<UserFacingError> = MutableStateFlow(UserFacingError.NoError)

    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(dispatcherProvider.IO, block = block)

    /**
     * Utility function to switch coroutine context to Main.
     * Useful for making UI updates from IO
     */
    suspend fun runOnMain(block: suspend CoroutineScope.() -> Unit) =
        withContext(dispatcherProvider.Main, block)

    fun <T> Result<T>.handleFailure(): Result<T> {
        exceptionOrNull()?.let { throwable ->
            when (throwable) {
                is ApiException -> {
                    errorStateFlow.value = UserFacingError.ApiError(code = throwable.code, title = R.string.api_error_title, description = R.string.api_error_description)
                }
                else -> {
                    errorStateFlow.value = UserFacingError.GeneralError(R.string.general_error_title, description = R.string.general_error_description)
                }
            }
        }
        return this
    }

    fun <T> Result<T>.logFailure(): Result<T> {
        exceptionOrNull()?.let { throwable ->
            when (throwable) {
                is ApiException -> {
                    // TODO: Abstract this out
                    Timber.d("${throwable.code}  ${throwable.responseBody}")
                }
                is IOException -> {
                    Timber.d("${throwable.message}")
                }
                else -> {
                    Timber.d("${throwable.message}")
                }
            }
        }
        return this
    }
}
