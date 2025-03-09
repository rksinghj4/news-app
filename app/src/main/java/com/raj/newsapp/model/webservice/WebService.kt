package com.raj.newsapp.model.webservice

import com.raj.newsapp.common.Constants.API_KEY
import com.raj.newsapp.model.data.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface WebService {

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines")
    suspend fun fetchTopHeadlines(@Query("country") country: String): TopHeadlinesResponse

}