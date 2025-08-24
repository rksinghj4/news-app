package com.raj.newsapp.model.webservice

import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.data.TopHeadlinesSourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): TopHeadlinesResponse

    @GET("top-headlines/sources")
    suspend fun fetchTopHeadlinesSources(): TopHeadlinesSourcesResponse

    @GET("top-headlines")
    suspend fun fetchTopHeadlinesBySource(@Query("sources") source: String): TopHeadlinesResponse

    @GET("top-headlines")
    suspend fun fetchTopHeadlinesByCountry(@Query("country") country: String): TopHeadlinesResponse

    @GET("top-headlines")
    suspend fun fetchTopHeadlinesByLanguage(@Query("language") language: String): TopHeadlinesResponse

    @GET("everything")
    suspend fun fetchNewsBySearchQuery(@Query("q") searchQueries: String): TopHeadlinesResponse
}