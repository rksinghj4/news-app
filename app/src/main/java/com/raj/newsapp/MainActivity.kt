package com.raj.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.raj.newsapp.ui.base.navigations.TopHeadlinesNaveHost
import com.raj.newsapp.ui.theme.NewsAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        TopHeadlinesNaveHost()
                    }
                    /*MainScreen(
                        msg = "Welcome to news app",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }
    }
}

@Composable
fun MainScreen(msg: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello, $msg!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppComposeTheme {
        MainScreen("Android")
    }
}