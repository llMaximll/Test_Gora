package com.github.llmaximll.core.data.remote.sources

import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.common.Country
import com.github.llmaximll.core.common.log
import com.github.llmaximll.core.data.remote.api_services.NewsApiService
import com.github.llmaximll.core.data.remote.models.ArticleDto
import javax.inject.Inject

interface NewsRemoteDataSource {

    suspend fun getTopHeadlines(
        country: Country = Country.Ru,
        pageSize: Int = 20,
        category: Category = Category.General
    ): Result<List<ArticleDto>>
}

class NewsRemoteDataSourceImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRemoteDataSource {

    override suspend fun getTopHeadlines(
        country: Country,
        pageSize: Int,
        category: Category
    ): Result<List<ArticleDto>> {
        val response = newsApiService.getTopHeadlines(
            country = country.name.lowercase(),
            pageSize = pageSize,
            category = category.name.lowercase()
        )
        val data = response.body()?.articles

        log("NewsRemoteDataSourceImpl::getTopHeadlines data:$data")

        return if (response.isSuccessful && !data.isNullOrEmpty()) {
            Result.success(data)
        } else {
            Result.failure(Throwable(response.message()))
        }
    }
}