package com.github.llmaximll.core.common.models

import com.github.llmaximll.core.common.Category
import kotlin.random.Random

data class Article(
    val id: Long,
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: Long,
    val content: String?,
    val category: Category
) {
    data class Source(
        val id: String?,
        val name: String?
    )

    companion object {
        val fakeArticles = listOf(
            Article(
                id = Random.nextLong(),
                source = Source(id = "google-news", name = "Google News"),
                author = "Авто Mail.ru",
                title = "В Англии нашли сарай с коллекцией редких ретроавтомобилей - Авто Mail.ru",
                description = null,
                url = "https://news.google.com/rss/articles/CBMiVmh0dHBzOi8vYXV0by5tYWlsLnJ1L2FydGljbGUvOTE2NDUtdi1hbmdsaWktbmFzaGxpLXNhcmFqLXMta29sbGVrdHNpZWotcmVka2loLXJldHJvYXYv0gFaaHR0cHM6Ly9hdXRvLm1haWwucnUvYW1wL2FydGljbGUvOTE2NDUtdi1hbmdsaWktbmFzaGxpLXNhcmFqLXMta29sbGVrdHNpZWotcmVka2loLXJldHJvYXYv?oc=5",
                urlToImage = null,
                publishedAt = System.currentTimeMillis(),
                content = null,
                category = Category.entries.random()
            ),
            Article(
                id = Random.nextLong(),
                source = Source(id = "google-news", name = "Google News"),
                author = "Хибины.ru",
                title = "Не свекла: популярный овощ вызывает резкий всплеск сахара в крови — влияет не только на диабетиков - новости Хибины.ru - Хибины.ru",
                description = null,
                url = "https://news.google.com/rss/articles/CBMingFodHRwczovL3d3dy5oaWJpbnkucnUvbXVybWFuc2theWEtb2JsYXN0L25ld3MvaXRlbS1uZS1zdmVrbGEtcG9wdWx5YXJueXktb3Zvc2hjaC12eXp5dmFldC1yZXpraXktdnNwbGVzay1zYWhhcmEtdi1rcm92aS12bGl5YWV0LW5lLXRvbGtvLW5hLWRpYWJldGlrb3YtMzIyNDM4L9IBAA?oc=5",
                urlToImage = null,
                publishedAt = System.currentTimeMillis(),
                content = null,
                category = Category.entries.random()
            ),
            Article(
                id = Random.nextLong(),
                source = Source(id = "google-news", name = "Google News"),
                author = "URA.RU",
                title = "Шойгу доложили, что ВСУ отброшены на 10 километров при освобождении Авдеевки - URA.RU",
                description = null,
                url = "https://news.google.com/rss/articles/CBMiIGh0dHBzOi8vdXJhLm5ld3MvbmV3cy8xMDUyNzM2NzQx0gEkaHR0cHM6Ly9hbXAudXJhLm5ld3MvbmV3cy8xMDUyNzM2NzQx?oc=5",
                urlToImage = null,
                publishedAt = System.currentTimeMillis(),
                content = null,
                category = Category.entries.random()
            )
        )
    }
}