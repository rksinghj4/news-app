package com.raj.newsapp.ui.base.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raj.newsapp.common.openCustomChromeTab
import com.raj.newsapp.view.SearchScreenNode
import kotlinx.serialization.Serializable

sealed interface SearchRoute {
    @Serializable
    data object SearchHome
}

@Composable
fun SearchNavHost() {
    val navHostController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navHostController, startDestination = SearchRoute.SearchHome) {
        composable<SearchRoute.SearchHome> {
            SearchScreenNode(onItemClick = { url ->
                openCustomChromeTab(context, url)
            })
        }
    }
}