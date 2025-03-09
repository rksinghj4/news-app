package com.raj.newsapp.common

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent


fun openCustomChromeTab(context: Context, url: String) {
    val customTabsIntentBuilder = CustomTabsIntent.Builder()
    val customTabsIntent: CustomTabsIntent = customTabsIntentBuilder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}