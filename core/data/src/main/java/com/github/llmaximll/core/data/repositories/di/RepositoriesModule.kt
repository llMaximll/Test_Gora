package com.github.llmaximll.core.data.repositories.di

import com.github.llmaximll.core.data.repositories.NewsRepository
import com.github.llmaximll.core.data.repositories.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository
}