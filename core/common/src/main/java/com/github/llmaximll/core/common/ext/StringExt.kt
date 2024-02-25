package com.github.llmaximll.core.common.ext

import com.github.llmaximll.core.common.err
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

fun String?.asDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())

    return try {
        format.parse(this ?: "")
    } catch (e: ParseException) {
        err(e)
        null
    }
}