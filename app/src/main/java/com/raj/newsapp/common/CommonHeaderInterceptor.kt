package com.raj.newsapp.common

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

//Common headers for all request
class CommonHeaderInterceptor(private val config: CoreConfig): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequestBuilder = originalRequest.newBuilder()
        //Add all required headers here
        newRequestBuilder.header(config.API_KEY, config.getAPIKey())
        newRequestBuilder.header(config.VERSION_CODE, config.getVersionCode())
        newRequestBuilder.header(config.VERSION_NAME, config.getVersionName())
        newRequestBuilder.header(config.OS, config.getOSVersion())

        newRequestBuilder.header(USER_AGENT_KEY, USER_AGENT)//Single place to change

        //Rebuild the new
        val newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)//Returns Response
    }

    companion object {
        //Keeping key & value in same file. Single place to change.
        private const val USER_AGENT_KEY = "User-Agent"
        private const val USER_AGENT = "news-android-app"
    }
}