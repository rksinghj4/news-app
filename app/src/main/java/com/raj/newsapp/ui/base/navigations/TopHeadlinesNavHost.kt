package com.raj.newsapp.ui.base.navigations

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
fun TopHeadlinesNavHost() {
    /**
     * 1. NavHostController - The Brain or navigation manager.
     * It knows the current screen and lets you navigate to other screens.
     */
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    /**
     * 2. NavHost — The Router
     *
     * It is where you define all your screens (destinations) and their routes.
     * It hosts your navigation graph.
     */
    NavHost(
        navController = navController,//must assign NavHostController
        startDestination = Route.TopHeadlines
    ) {
        /**
         * 3. composable() — A Destination
         * You can define as many nodes/composable you want in side NavHost
         */
        composable<Route.TopHeadlines> { navBackStackEntry ->
            TopHeadlinesNode(onNewsClick = { url ->
                openCustomChromeTab(context, url)
            }
            )
        }
        //Keep adding other composable
    }
}


