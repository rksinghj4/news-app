package com.raj.newsapp.ui.base.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.raj.newsapp.common.openCustomChromeTab
import com.raj.newsapp.view.NewsBySourceNode
import com.raj.newsapp.view.NewsSourcesNode
import kotlinx.serialization.Serializable

sealed interface SourceRoute {
    @Serializable
    data object SourcesList

    @Serializable
    data class SourceDetails(val source: String)
}


@Composable
fun SourcesNavHost() {
    //NavHostController - Brain or manager, who remember the current screen and lets you navigate to other screens.
    val navHostController: NavHostController = rememberNavController()
    val context = LocalContext.current
    //NavHost is nothing but a navigation graph, where each node is nothing but a screen.
    NavHost(//Graph
        navController = navHostController,
        startDestination = SourceRoute.SourcesList
    ) {
        //Node 1
        composable<SourceRoute.SourcesList> {
            //Screen 1
            NewsSourcesNode(onItemClick = { source ->
                //Brain or manager will decide the next destination node.
                navHostController.navigate(SourceRoute.SourceDetails(source))
            })
        }

        //Node 2
        composable<SourceRoute.SourceDetails> { navBackStackEntry ->
            //Transfer data from one screen to another screen
            val sourceDetails = navBackStackEntry.toRoute<SourceRoute.SourceDetails>()

            NewsBySourceNode(selectedSource = sourceDetails.source, onItemClick = { url ->
                openCustomChromeTab(context, url)
            })
        }
    }
}