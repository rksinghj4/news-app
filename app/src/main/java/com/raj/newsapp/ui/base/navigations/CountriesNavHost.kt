package com.raj.newsapp.ui.base.navigations

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.raj.newsapp.common.openCustomChromeTab
import com.raj.newsapp.view.CountryNode
import com.raj.newsapp.view.NewsByCountry
import kotlinx.serialization.Serializable

sealed interface CountryRoute {
    @Serializable
    data object CountriesList

    @Serializable
    data class CountryDetails(val countryCode: String)
}

@Composable
fun CountriesNavHost() {
    val navHostController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navHostController, startDestination = CountryRoute.CountriesList) {
        composable<CountryRoute.CountriesList> {
            CountryNode(
                onItemClick = { countryCode ->
                    navHostController.navigate(CountryRoute.CountryDetails(countryCode = countryCode))
                }
            )
        }

        composable<CountryRoute.CountryDetails> { navBackStackEntry ->
            val countryDetails = navBackStackEntry.toRoute<CountryRoute.CountryDetails>()
            NewsByCountry(selectedCountry = countryDetails.countryCode, onItemClick = { url ->
                openCustomChromeTab(context, url)
            })
        }
    }
}