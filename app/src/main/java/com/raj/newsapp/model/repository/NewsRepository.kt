package com.raj.newsapp.model.repository

import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.data.TopHeadlinesSourcesResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun fetchTopHeadlines(country: String): Flow<TopHeadlinesResponse>

    fun fetchNewsSources(): Flow<TopHeadlinesSourcesResponse>

    fun fetchTopHeadlinesBySource(source: String): Flow<TopHeadlinesResponse>

}