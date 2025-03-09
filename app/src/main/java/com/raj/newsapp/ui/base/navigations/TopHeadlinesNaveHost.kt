package com.raj.newsapp.ui.base.navigations

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raj.newsapp.common.openCustomChromeTab
import com.raj.newsapp.view.TopHeadlinesNode
import kotlinx.serialization.Serializable


sealed class Route {
    @Serializable
    data object TopHeadlines : Route()
}

@Composable
fun TopHeadlinesNaveHost() {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,//must assign NavHostController
        startDestination = Route.TopHeadlines
    ) {
        composable<Route.TopHeadlines> { navBackStackEntry ->
            TopHeadlinesNode(onNewsClick = { url ->
                openCustomChromeTab(context, url)
            }
            )
        }
    }
}


