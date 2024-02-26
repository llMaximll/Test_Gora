package com.github.llmaximll.core.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object ImageCache {
    private val memoryCache: LruCache<String, Bitmap> = LruCache(10) // 24 Мб

    fun get(key: String): Bitmap? {
        val result = memoryCache.get(key)

        log("ImageCache::get (result != null): ${result != null}")

        return result
    }

    fun put(key: String, bitmap: Bitmap) {
        memoryCache.put(key, bitmap)
    }
}

suspend fun Context.saveToDiskCache(
    url: String,
    bitmap: Bitmap,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) = withContext(dispatcher) {
    val context = this@saveToDiskCache

    val fileName = url.hashCode().toString()
    val cacheDir = context.cacheDir

    try {
        val file = File(cacheDir, fileName)
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
    } catch (e: IOException) {
        err(e)
    }
}

suspend fun Context.readFromDiskCache(
    url: String,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Bitmap? = withContext(dispatcher) {
    val context = this@readFromDiskCache

    val fileName = url.hashCode().toString()
    val cacheDir = context.cacheDir

    try {
        val file = File(cacheDir, fileName)
        if (file.exists()) {
            FileInputStream(file).use { fis ->
                val result = BitmapFactory.decodeStream(fis)

                log("readFromDiskCache:: (result != null): ${result != null}")

                return@withContext result
            }
        }
    } catch (e: IOException) {
        err(e)
    }

    return@withContext null
}

suspend fun fetchImage(
    urlString: String,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Bitmap = withContext(dispatcher) {
    val url = URL(urlString)
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

    connection.doInput = true
    connection.connectTimeout = 5000
    connection.readTimeout = 5000

    connection.connect()

    return@withContext BitmapFactory.decodeStream(connection.inputStream)
}