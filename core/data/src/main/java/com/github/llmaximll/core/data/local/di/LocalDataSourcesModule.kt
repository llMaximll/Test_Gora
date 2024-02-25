package com.github.llmaximll.core.data.local.di

import com.github.llmaximll.core.data.local.sources.NewsLocalDataSource
import com.github.llmaximll.core.data.local.sources.NewsLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourcesModule {

    @Binds
    fun bindNewsLocalDataSource(
        impl: NewsLocalDataSourceImpl
    ): NewsLocalDataSource
}