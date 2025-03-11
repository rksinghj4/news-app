package com.raj.newsapp.ui.base.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.raj.newsapp.common.openCustomChromeTab
import com.raj.newsapp.view.LanguageNode
import com.raj.newsapp.view.NewsByLanguageNode
import kotlinx.serialization.Serializable

sealed interface LanguageRoutes {
    @Serializable
    data object LanguageList

    @Serializable
    data class LanguageDetails(val selectedLanguage: String)
}

@Composable
fun LanguageNavHost() {
    val navHostController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navHostController, startDestination = LanguageRoutes.LanguageList) {
        composable<LanguageRoutes.LanguageList> {
            LanguageNode(onItemClick = { selectedLanguage ->
                navHostController.navigate(LanguageRoutes.LanguageDetails(selectedLanguage))
            })
        }

        composable<LanguageRoutes.LanguageDetails> { navBackStackEntry ->
            val languageDetails = navBackStackEntry.toRoute<LanguageRoutes.LanguageDetails>()
            NewsByLanguageNode(
                selectedLanguage = languageDetails.selectedLanguage,
                onItemClick = { url ->
                    openCustomChromeTab(context, url)
                }
            )
        }
    }
}