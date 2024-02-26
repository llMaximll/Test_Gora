package com.github.llmaximll.core.data.remote.di

import com.github.llmaximll.core.data.remote.sources.NewsRemoteDataSource
import com.github.llmaximll.core.data.remote.sources.NewsRemoteDataSourceImpl
import com.github.llmaximll.core.data.repositories.NewsRepository
import com.github.llmaximll.core.data.repositories.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteDataSourcesModule {

    @Binds
    fun bindNewsRemoteDataSource(
        impl: NewsRemoteDataSourceImpl
    ): NewsRemoteDataSource
}