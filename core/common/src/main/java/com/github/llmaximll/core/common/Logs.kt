package com.github.llmaximll.core.common

import timber.log.Timber
import java.lang.Exception

fun log(message: String) {
    Timber.v(message)
}

fun err(message: String) {
    Timber.e(message)
}

fun err(exception: Exception) {
    Timber.e(exception)
}