package com.github.llmaximll.core.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.common.models.Article

@Entity("articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @Embedded("source_")
    val source: SourceEntity?,
    @ColumnInfo("author") val author: String?,
    @ColumnInfo("title") val title: String?,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("url") val url: String?,
    @ColumnInfo("urlToImage") val urlToImage: String?,
    @ColumnInfo("publishedAt") val publishedAt: Long,
    @ColumnInfo("content") val content: String?,
    @ColumnInfo("category") val category: Category
) {
    data class SourceEntity(
        @ColumnInfo("id") val id: String?,
        @ColumnInfo("name") val name: String?
    )

    companion object {
        fun ArticleEntity.asModel() =
            Article(
                id = this.id,
                source = this.source.asModel(),
                author = this.author,
                title = this.title,
                description = this.description,
                url = this.url,
                urlToImage = this.urlToImage,
                publishedAt = this.publishedAt,
                content = this.content,
                category = this.category
            )

        private fun SourceEntity?.asModel() =
            Article.Source(
                id = this?.id,
                name = this?.name
            )
    }
}