package com.github.llmaximll.core.data.local.di

import android.content.Context
import androidx.room.Room
import com.github.llmaximll.core.data.local.daos.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDb =
        Room.databaseBuilder(
            context,
            AppDb::class.java,
            AppDb.DB_NAME
        )
            .build()

    @Provides
    fun provideNewsDao(appDb: AppDb): NewsDao = appDb.newsDao()
}