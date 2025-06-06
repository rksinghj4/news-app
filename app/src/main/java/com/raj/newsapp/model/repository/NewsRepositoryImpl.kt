package com.raj.newsapp.model.repository

import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.data.TopHeadlinesSourcesResponse
import com.raj.newsapp.model.webservice.WebService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Separation of Concerns
 * Your repository should focus on business logic and data source coordination.
 *
 * It should not be tied to how concurrency is managed (Dispatchers.IO, etc.).
 *
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(private val webService: WebService) : NewsRepository {
    override fun fetchTopHeadlines(country: String): Flow<TopHeadlinesResponse> {
        return flow {
            emit(webService.fetchTopHeadlines(country))
        }
    }

    override fun fetchNewsSources(): Flow<TopHeadlinesSourcesResponse> {
        return flow {
            emit(webService.fetchTopHeadlinesSources())
        }
    }

    override fun fetchTopHeadlinesBySource(source: String): Flow<TopHeadlinesResponse> {
        return flow {
            emit(webService.fetchTopHeadlinesBySource(source))
        }


    }

    override fun fetchTopHeadlinesByCountry(countryCode: String): Flow<TopHeadlinesResponse> {
        return flow {
            emit(webService.fetchTopHeadlinesByCountry(countryCode))
        }
    }

    override fun fetchTopHeadlinesByLanguage(language: String): Flow<TopHeadlinesResponse> {
        return flow {
            emit(webService.fetchTopHeadlinesByLanguage(language))
        }
    }

    override fun fetchNewsBySearchQuery(searchQueries: String): Flow<TopHeadlinesResponse> {
        return flow {
            emit(webService.fetchNewsBySearchQuery(searchQueries))
        }
    }


}