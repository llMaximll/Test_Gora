package com.github.llmaximll.core.data.local.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.llmaximll.core.data.BuildConfig
import com.github.llmaximll.core.data.local.daos.NewsDao
import com.github.llmaximll.core.data.local.models.ArticleEntity

@Database(
    entities = [
        ArticleEntity::class
    ],
    version = BuildConfig.DB_VERSION,
    exportSchema = true
)
abstract class AppDb : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        const val DB_NAME = "test_gora.db"
    }
}