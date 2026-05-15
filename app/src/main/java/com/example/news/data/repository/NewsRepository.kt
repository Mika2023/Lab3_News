package com.example.news.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.news.config.AppConfig
import com.example.news.data.client.RetrofitClient
import com.example.news.data.dto.NewsArticle
import com.example.news.data.paging.NewsApiPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepository {
    private val newsApi = RetrofitClient.newsApiClient

    suspend fun getTopHeadlines(category: String = "general"): List<NewsArticle> {
        return try {
            val response = newsApi.getTopHeadlines(category = category)
            response.articles.filter { it.title != "[Removed]" }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getPagedNews(query: String): Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(
                pageSize = AppConfig.PAGE_SIZE_COMMON,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsApiPagingSource(newsApi, query) }
        ).flow
    }
}