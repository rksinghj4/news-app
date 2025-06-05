package com.raj.newsapp.common

import android.content.Context
import okhttp3.Interceptor

interface CoreConfig {
    val API_KEY: String
        get() = "X-Api-Key" //Property initializers are not allowed
    val VERSION_NAME: String
        get() = "VERSION_NAME"

    val VERSION_CODE: String
        get() = "VERSION_CODE"
    val OS: String
        get() = "OS"
    fun getApplicationContext(): Context
    fun getAPIKey(): String
    fun getBaseUrl(): String
    fun getGraphQLBaseUrl(): String
    //Common interceptors
    fun getInterceptors(): List<Interceptor> {
        return arrayListOf()
    }

    fun getVersionName(): String
    fun getVersionCode(): String
    fun getOSVersion(): String
    fun getCacheDirPath(): String
}