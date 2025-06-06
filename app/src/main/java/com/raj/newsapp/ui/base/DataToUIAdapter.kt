package com.raj.newsapp.ui.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.raj.newsapp.model.data.TopHeadlinesResponse

@Composable
fun ArticleList(articles: List<TopHeadlinesResponse.Article>, onItemClick: (url: String) -> Unit) {
    LazyColumn(modifier = Modifier.padding(24.dp)) {
        items(items = articles, key = { article -> article.url + article.publishedAt }) { article ->
            Article(article) { url ->
                onItemClick(url)
            }
        }
    }
}

@Composable
fun Article(article: TopHeadlinesResponse.Article, onItemClick: (url: String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            if (article.url.isNotEmpty()) {
                onItemClick(article.url)
            }
        }) {
        BannerImage(article)
        if (article.title.isNotEmpty()) {
            TitleText(article.title)
        }
        if (article.description.isNotEmpty()) {
            DescriptionText(article.description)
        }
        if (article.source.name.isNotEmpty()) {
            SourceText(article.source.name)
        }
    }
}

/**
 * Image Loading in Jetpack Compose
 * Jetpack Compose doesn’t have a built-in image loading solution.
 * If your app needs to load images (e.g., from the internet, local storage, or resources)
 * and display them in a Compose UI, you need a library like Coil.
 *
 * The coil-compose module provides Composables like AsyncImage,
 * which simplifies loading and displaying images directly in your Compose code.
 */

/**
 * Key Features of Coil with Compose:
 * 1. Asynchronous Loading: Coil leverages Kotlin Coroutines to load images off the main thread,
 * ensuring smooth UI performance.
 * 2. Integration with Compose: The coil-compose library offers a declarative API that fits naturally
 * into Compose’s reactive paradigm.
 * 3. Customization: It supports placeholders, error states, transformations (e.g., rounding corners),
 * and caching, all configurable within Compose.
 * 4. Efficiency: Coil is lightweight (compared to alternatives like Glide or Picasso)
 * and has built-in memory and disk caching to optimize resource usage.
 */
@Composable
fun BannerImage(article: TopHeadlinesResponse.Article) {
    AsyncImage(
        model = article.urlToImage,
        contentDescription = article.description,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun TitleText(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.Black,
        maxLines = 2
    )
}

@Composable
fun DescriptionText(desc: String) {
    Text(
        text = desc,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        maxLines = 2
    )
}

@Composable
fun SourceText(source: String) {
    Text(
        text = source,
        style = MaterialTheme.typography.titleSmall,
        color = Color.DarkGray,
        maxLines = 2
    )
}
