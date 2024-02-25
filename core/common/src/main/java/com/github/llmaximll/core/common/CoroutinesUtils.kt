package com.github.llmaximll.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

fun ViewModel.launchWithHandler(
    dispatcher: CoroutineDispatcher,
    onException: (Throwable) -> Unit = { },
    block: suspend () -> Unit
) =
    viewModelScope.launch(
        dispatcher + CoroutineExceptionHandler { _, throwable ->
            throwable.message?.let { err(it) }
            onException(throwable)
        }
    ) {
        block()
    }