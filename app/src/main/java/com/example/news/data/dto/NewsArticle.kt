package com.example.news.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsArticle (
    val source: Source,
    val author: String? = "Аноним",
    val title: String,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String? = null
)