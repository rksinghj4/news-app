package com.raj.newsapp.model.repository

import com.raj.newsapp.model.data.TopHeadlinesResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun fetchTopHeadlines(country: String): Flow<TopHeadlinesResponse>
}