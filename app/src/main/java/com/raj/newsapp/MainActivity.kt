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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.newsapp.ui.base.navigations.TopHeadlinesNaveHost
import com.raj.newsapp.ui.theme.NewsAppComposeTheme
import com.raj.newsapp.view.ClickAction
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
            val showTopHeadlineState by viewModel.showTopHeadlines.collectAsStateWithLifecycle()
            val showMainScreenState by viewModel.showMainScreen.collectAsStateWithLifecycle()

            val context = LocalContext.current
            var backPressedTime by remember { mutableStateOf(0L) }

            NewsAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        BackHandler {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - backPressedTime < 2000) {
                                (context as? Activity)?.finish()
                            } else {
                                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                                backPressedTime = currentTime
                            }
                            viewModel.showMainScreen()
                        }
                        if (showMainScreenState) {
                            MainScreen(clickAction(viewModel))
                        }
                        if (showTopHeadlineState) {
                            TopHeadlinesNaveHost()
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

fun clickAction(viewModel: MainViewModel) = ClickAction(
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