package com.raj.newsapp.common

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.ColorScheme
import androidx.compose.ui.graphics.Color
import com.raj.newsapp.R


fun openCustomChromeTab(context: Context, url: String) {
    val customTabsIntentBuilder = CustomTabsIntent.Builder()
    customTabsIntentBuilder.setShowTitle(true)
    customTabsIntentBuilder.setToolbarColor(context.getColor(R.color.teal_200))
    val customTabsIntent: CustomTabsIntent = customTabsIntentBuilder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}