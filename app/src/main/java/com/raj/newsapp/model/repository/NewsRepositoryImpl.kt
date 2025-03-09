package com.raj.newsapp.model.repository

import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.webservice.WebService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(private val webService: WebService): NewsRepository {
    override fun fetchTopHeadlines(country: String): Flow<TopHeadlinesResponse> {
        return flow {
            emit(webService.fetchTopHeadlines(country))
        }
    }
}