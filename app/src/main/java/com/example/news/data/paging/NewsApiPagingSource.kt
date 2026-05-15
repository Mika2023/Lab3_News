package com.example.news.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.news.data.client.NewsApiClient
import com.example.news.data.dto.NewsArticle

class NewsApiPagingSource (
    private val newsApiClient: NewsApiClient,
    private val searchQuery: String
) : PagingSource<Int, NewsArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val pageNumber = params.key ?: 1
        return try {
            val response = newsApiClient.searchNews(
                query = searchQuery,
                page = pageNumber
            )

            val articles = response.articles.filter { it.title != "[Removed]" }

            LoadResult.Page(
                data = articles,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (articles.isEmpty()) null else pageNumber + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}