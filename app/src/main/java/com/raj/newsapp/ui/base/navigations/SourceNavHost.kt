package com.raj.newsapp.ui.base.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raj.newsapp.view.NewsSourceNode
import kotlinx.serialization.Serializable

sealed interface SourceRoute {
    @Serializable
    data object Home
}

@Composable
fun SourceNavHost() {
    val navHostController: NavHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = SourceRoute.Home
    ) {
        composable<SourceRoute.Home> {
            NewsSourceNode(onItemClick = {
                //navHostController.navigate()
            })
        }
    }
}