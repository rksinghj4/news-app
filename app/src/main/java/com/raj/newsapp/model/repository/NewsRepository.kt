package com.raj.newsapp.model.repository

import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.data.TopHeadlinesSourceResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun fetchTopHeadlines(country: String): Flow<TopHeadlinesResponse>

    fun fetchNewsSources(): Flow<TopHeadlinesSourceResponse>
}