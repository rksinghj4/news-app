package com.raj.newsapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ClickActionMain(
    val topHeadlines: () -> Unit,
    val newsSource: () -> Unit,
    val countries: () -> Unit,
    val language: () -> Unit,
    val search: () -> Unit
)

@Composable
fun MainScreen(clickActionMain: ClickActionMain) {

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardItem(text = "Top Headlines", onClick = clickActionMain.topHeadlines)

        CardItem(text = "News Source", onClick = clickActionMain.newsSource)

        CardItem(text = "Countries", onClick = clickActionMain.countries)

        CardItem(text = "Language", onClick = clickActionMain.language)

        CardItem(text = "Search", onClick = clickActionMain.search)
    }
}

@Composable
fun CardItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(60.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text, color = Color.White)
        }
    }
}