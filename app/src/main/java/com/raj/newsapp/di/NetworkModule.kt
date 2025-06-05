package com.raj.newsapp.di

import android.content.Context
import com.raj.newsapp.common.CommonHeaderInterceptor
import com.raj.newsapp.common.CoreConfig
import com.raj.newsapp.common.CoreConfigImpl
import com.raj.newsapp.common.DefaultDispatcherProvider
import com.raj.newsapp.common.DispatcherProvider
import com.raj.newsapp.model.webservice.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {//Note: It's class
    //Because Hilt binds applicationContext automatically in the SingletonComponent,
    // you don’t need to provide it yourself.
    @Singleton
    @Provides
    fun provideCoreConfig(@ApplicationContext context: Context) : CoreConfig = CoreConfigImpl(context)

    @Singleton
    @Provides
    fun provideInterceptor(config: CoreConfig) = CommonHeaderInterceptor(config)

    @Singleton
    @Provides
    @BaseUrl
    fun provideBaseUrl(config: CoreConfig) = config.getBaseUrl()

    @Singleton
    @Provides
    fun provideGsonConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    /**
     * If we want to add interceptors then we have to build OkHttpClient object manually
     * then add interceptor in it.
     *
     * If we don't want to add interceptor then we don't need
     * to build and add okHttpClient manually while building Retrofit object below
     */
    @Singleton
    @Provides
    fun provideOkHttpCache(coreConfig: CoreConfig): Cache {
        val cacheSize = 10 * 1024 * 1024
        val nwCacheFolder = File(coreConfig.getCacheDirPath(), "NewsCache")
        nwCacheFolder.mkdir()
        return Cache(nwCacheFolder, cacheSize.toLong())
    }


    //To provide different variations of OkHttpClient we can write separate methods
    // to customize the builder and build it.
    @Singleton
    @Provides
    fun provideOkHttpBuilder(cache: Cache, coreConfig: CoreConfig):OkHttpClient.Builder {
        //All common stuffs are added here.
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(CommonHeaderInterceptor(coreConfig))
        return okHttpClientBuilder
    }

    @Singleton
    @Provides
    fun okHttpClient(okHttpBuilder: OkHttpClient.Builder): OkHttpClient {
        //Here we have option to add specific Interceptors and time outs etc.
        okHttpBuilder.connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): WebService = Retrofit.Builder()
        .baseUrl(baseUrl)//1
        .client(okHttpClient)//2 (Retrofit is using Okhttp via api, so we don't need to add it in gradle again)
        .addConverterFactory(gsonConverterFactory)//3
        .build()//3
        .create(WebService::class.java)//4 create implementation of our WebService interface

    @Singleton
    @Provides
    fun dispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    companion object {
        //Time to establish connection with the serve, Use shorter connectTimeout to fail fast on network errors
        private const val CONNECTION_TIME_OUT =  10L
        //Max time to wait for server response (after connection is made)
        private const val READ_TIME_OUT =  30L
        //Max time allowed to send data to server (like file uploads)
        private const val WRITE_TIME_OUT =  30L
    }
}


