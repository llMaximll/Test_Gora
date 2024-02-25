package com.github.llmaximll.core.data.local.sources

import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.data.local.daos.NewsDao
import com.github.llmaximll.core.data.local.models.ArticleEntity
import javax.inject.Inject

interface NewsLocalDataSource {

    suspend fun insertArticles(articles: List<ArticleEntity>)

    suspend fun getAllArticlesByCategory(category: Category): List<ArticleEntity>
}

class NewsLocalDataSourceImpl @Inject constructor(
    private val newsDao: NewsDao
) : NewsLocalDataSource {

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        newsDao.insertArticles(articles)
    }

    override suspend fun getAllArticlesByCategory(category: Category): List<ArticleEntity> =
        newsDao.getAllArticlesByCategory(category = category)
}