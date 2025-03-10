package com.raj.newsapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.newsapp.ui.base.navigations.CountriesNavHost
import com.raj.newsapp.ui.base.navigations.SourcesNavHost
import com.raj.newsapp.ui.base.navigations.TopHeadlinesNavHost
import com.raj.newsapp.ui.theme.NewsAppComposeTheme
import com.raj.newsapp.view.ClickActionMain
import com.raj.newsapp.view.MainScreen
import com.raj.newsapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val showMainScreenState by viewModel.showMainScreen.collectAsStateWithLifecycle()

            val showTopHeadlineState by viewModel.showTopHeadlines.collectAsStateWithLifecycle()
            val showNewsSourcesState by viewModel.showNewsSource.collectAsStateWithLifecycle()
            val showCountriesState by viewModel.showCountries.collectAsStateWithLifecycle()
            val context = LocalContext.current
            var backPressedTime by remember { mutableLongStateOf(0L) }

            NewsAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        BackHandler {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - backPressedTime < 2000) {
                                (context as? Activity)?.finish()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Press back again to exit",
                                    Toast.LENGTH_SHORT
                                ).show()
                                backPressedTime = currentTime
                            }
                            viewModel.showMainScreen()
                        }
                        if (showMainScreenState) {
                            MainScreen(clickAction(viewModel))
                        }
                        if (showTopHeadlineState) {
                            TopHeadlinesNavHost()
                        }
                        if (showNewsSourcesState) {
                            SourcesNavHost()
                        }
                        if (showCountriesState) {
                            CountriesNavHost()
                        }
                    }
                }
            }
        }
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()
    }
}

fun clickAction(viewModel: MainViewModel) = ClickActionMain(
    topHeadlines = {
        viewModel.showTopHeadlines()
    },
    newsSource = {
        viewModel.showNewsSource()
    },
    countries = {
        viewModel.showCountries()
    },
    language = {
        viewModel.showLanguage()
    },
    search = {
        viewModel.showSearch()
    }
)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppComposeTheme {
        MainScreen(clickAction(hiltViewModel()))
    }
}