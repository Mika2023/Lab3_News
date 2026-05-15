package com.example.news.data.client

import com.example.news.config.AppConfig
import com.example.news.data.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiClient {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String = "general",
        @Query("pageSize") pageSize: Int = AppConfig.PAGE_SIZE_HEADLINE
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String = "Russia",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = AppConfig.PAGE_SIZE_COMMON
    ): NewsResponse
}