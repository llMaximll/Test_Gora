package com.github.llmaximll.core.data.remote.api_services

import com.github.llmaximll.core.data.remote.models.TopHeadlinesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String,
    ): Response<TopHeadlinesDto>
}