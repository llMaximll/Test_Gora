package com.github.llmaximll.core.data.repositories

import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.common.Country
import com.github.llmaximll.core.common.di.IoDispatcher
import com.github.llmaximll.core.common.log
import com.github.llmaximll.core.common.models.Article
import com.github.llmaximll.core.data.local.models.ArticleEntity.Companion.asModel
import com.github.llmaximll.core.data.local.sources.NewsLocalDataSource
import com.github.llmaximll.core.data.remote.models.ArticleDto.Companion.asEntity
import com.github.llmaximll.core.data.remote.models.ArticleDto.Companion.asModel
import com.github.llmaximll.core.data.remote.sources.NewsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NewsRepository {

    suspend fun getTopHeadlinesByCategory(
        country: Country = Country.Ru,
        pageSize: Int = 20,
        category: Category = Category.General
    ): Result<List<Article>>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {

    override suspend fun getTopHeadlinesByCategory(
        country: Country,
        pageSize: Int,
        category: Category
    ): Result<List<Article>> = withContext(ioDispatcher) {
        val localResult = newsLocalDataSource.getAllArticlesByCategory(category)

        if (localResult.isNotEmpty()) {
            log("NewsRepositoryImpl::getTopHeadlines Local result: $localResult")
            Result.success(localResult.map { it.asModel() })
        } else {
            val remoteResult = newsRemoteDataSource.getTopHeadlines(
                category = category,
                country = Country.Us
            )
            val data = remoteResult.getOrNull()

            log("NewsRepositoryImpl::getTopHeadlines Remote result: $remoteResult")

            if (remoteResult.isSuccess && !data.isNullOrEmpty()) {
                newsLocalDataSource.insertArticles(data.map { it.asEntity(category) })

                Result.success(data.map { it.asModel(category) })
            } else {
                Result.failure(NoSuchElementException())
            }
        }
    }
}