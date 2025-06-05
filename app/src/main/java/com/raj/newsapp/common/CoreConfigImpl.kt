package com.raj.newsapp.common

import android.content.Context
import android.os.Build
import com.raj.newsapp.BuildConfig
import com.raj.newsapp.NewsApplication

class CoreConfigImpl(private val applicationContext: Context): CoreConfig {
    override fun getApplicationContext(): Context = applicationContext

    override fun getAPIKey(): String = BuildConfig.API_KEY//Secure it

    override fun getBaseUrl(): String = BuildConfig.BASE_URL//Secure it

    override fun getGraphQLBaseUrl(): String {//Secure it
        TODO("Not yet implemented")
    }

    override fun getVersionName(): String = BuildConfig.VERSION_NAME

    override fun getVersionCode(): String = BuildConfig.VERSION_CODE.toString()

    override fun getOSVersion(): String {
        return Build.VERSION.SDK_INT.toString()
    }

    override fun getCacheDirPath(): String {
        return applicationContext.cacheDir.path
    }
}