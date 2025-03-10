package com.raj.newsapp.model.webservice

import com.raj.newsapp.common.Constants.API_KEY
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.data.TopHeadlinesSourcesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WebService {

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): TopHeadlinesResponse

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines/sources")
    suspend fun fetchTopHeadlinesSources(): TopHeadlinesSourcesResponse

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines")
    suspend fun fetchTopHeadlinesBySource(@Query("sources") source: String): TopHeadlinesResponse

    @Headers("X-Api-Key: $API_KEY", "User-Agent: news-android-app")
    @GET("top-headlines")
    suspend fun fetchTopHeadlinesByCountry(@Query("country") country: String): TopHeadlinesResponse
}