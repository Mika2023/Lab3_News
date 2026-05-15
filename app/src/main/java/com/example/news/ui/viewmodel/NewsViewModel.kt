package com.example.news.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.data.dto.NewsArticle
import com.example.news.data.repository.NewsRepository
import com.example.news.enums.TopHeadlineCategories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepository: NewsRepository = NewsRepository()
): ViewModel() {
    private val _topHeadlines = MutableStateFlow<List<NewsArticle>>(emptyList())
    val topHeadlines: StateFlow<List<NewsArticle>> = _topHeadlines.asStateFlow()

    private val _isLoadingTop = MutableStateFlow(false)
    val isLoadingTop: StateFlow<Boolean> = _isLoadingTop.asStateFlow()

    val categories = TopHeadlineCategories.entries.toTypedArray()

    private val _selectedCategory = MutableStateFlow(categories[0])
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedNews: Flow<PagingData<NewsArticle>> = _searchQuery
        .debounce(500L)
        .map { query ->
            query.ifBlank { "world" }
        }
        .flatMapLatest { query ->
            newsRepository.getPagedNews(query = query)
        }.cachedIn(viewModelScope)

    init {
        fetchTopHeadlines(categories[0])
    }

    fun fetchTopHeadlines(category: TopHeadlineCategories) {
        viewModelScope.launch {
            _selectedCategory.value = category
            try {
                _isLoadingTop.value = true
                val news = newsRepository.getTopHeadlines(category = _selectedCategory.value.enName)
                _topHeadlines.value = news
                _isLoadingTop.value = false
            } catch (e: Exception) {
                _topHeadlines.value = emptyList()
            }

        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}