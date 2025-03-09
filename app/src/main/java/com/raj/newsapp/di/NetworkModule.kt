package com.raj.newsapp.di

import com.raj.newsapp.model.webservice.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {//Note: It's class

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl() = "https://newsapi.org/v2/"

    @Singleton
    @Provides
    fun provideGsonConvertorFactory() = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)//1
        .addConverterFactory(gsonConverterFactory)//2
        .build()//3
        .create(WebService::class.java)//4 create implementation of our WebService interface

}