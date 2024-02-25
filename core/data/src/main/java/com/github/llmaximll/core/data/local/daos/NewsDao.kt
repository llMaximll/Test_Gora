package com.github.llmaximll.core.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.data.local.models.ArticleEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query(
        """
        SELECT *
        FROM articles
        WHERE category = :category
    """
    )
    suspend fun getAllArticlesByCategory(category: Category): List<ArticleEntity>
}