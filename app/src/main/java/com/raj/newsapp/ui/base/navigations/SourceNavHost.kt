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
fun SourceNavHost() {
    val navHostController: NavHostController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        navController = navHostController,
        startDestination = SourceRoute.SourcesList
    ) {
        composable<SourceRoute.SourcesList> {
            NewsSourcesNode(onItemClick = { source ->
                navHostController.navigate(SourceRoute.SourceDetails(source))
            })
        }

        composable<SourceRoute.SourceDetails> { navBackStackEntry ->
            val sourceDetails = navBackStackEntry.toRoute<SourceRoute.SourceDetails>()

            NewsBySourceNode(selectedSource = sourceDetails.source, onItemClick = { url ->
                openCustomChromeTab(context, url)
            })
        }
    }
}