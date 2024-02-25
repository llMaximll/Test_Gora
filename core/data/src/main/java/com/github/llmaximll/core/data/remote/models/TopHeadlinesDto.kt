package com.github.llmaximll.core.data.remote.models

import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.common.ext.asDate
import com.github.llmaximll.core.common.models.Article
import com.github.llmaximll.core.data.local.models.ArticleEntity
import com.github.llmaximll.core.data.remote.models.ArticleDto.Companion.asModel
import com.google.gson.annotations.SerializedName
import java.util.Date

data class TopHeadlinesDto(
    @SerializedName("status") val status: String?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<ArticleDto>
)

data class ArticleDto(
    @SerializedName("source") val source: SourceDto?,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?,
) {
    data class SourceDto(
        @SerializedName("id") val id: String?,
        @SerializedName("name") val name: String?
    )

    companion object {
        fun ArticleDto.asModel(category: Category) =
            Article(
                id = 0L,
                source = this.source.asModel(),
                author = this.author,
                title = this.title,
                description = this.description,
                url = this.url,
                urlToImage = this.urlToImage,
                publishedAt = (this.publishedAt.asDate() ?: Date(0)).time,
                content = this.content,
                category = category
            )

        private fun SourceDto?.asModel() =
            Article.Source(
                id = this?.id,
                name = this?.name
            )

        fun ArticleDto.asEntity(category: Category) =
            ArticleEntity(
                id = 0L,
                source = this.source.asEntity(),
                author = this.author,
                title = this.title,
                description = this.description,
                url = this.url,
                urlToImage = this.urlToImage,
                publishedAt = (this.publishedAt.asDate() ?: Date(0)).time,
                content = this.content,
                category = category
            )

        private fun SourceDto?.asEntity() =
            ArticleEntity.SourceEntity(
                id = this?.id,
                name = this?.name
            )
    }
}