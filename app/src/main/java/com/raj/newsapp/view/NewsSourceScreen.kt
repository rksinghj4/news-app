package com.raj.newsapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.newsapp.R
import com.raj.newsapp.model.data.TopHeadlinesSourceResponse
import com.raj.newsapp.ui.base.ErrorScreen
import com.raj.newsapp.ui.base.Loading
import com.raj.newsapp.ui.base.TopBarScaffold
import com.raj.newsapp.ui.base.UiState
import com.raj.newsapp.viewmodel.TopHeadlinesSourcesViewModel

@Composable
fun NewsSourceNode(
    onItemClick: () -> Unit = {},
    viewModel: TopHeadlinesSourcesViewModel = hiltViewModel()
) {
    val sourceState by viewModel.sourceStateFlow.collectAsStateWithLifecycle()

    TopBarScaffold(title = stringResource(R.string.top_headlines_sources)) {
        NewsSourceScreen(sourceState, onItemClick)
    }
}

@Composable
fun NewsSourceScreen(
    uiState: UiState<List<TopHeadlinesSourceResponse.Source>>,
    onItemClick: () -> Unit
) {
    when (uiState) {
        UiState.Loading -> {
            Loading()
        }

        is UiState.Success -> {
            SourcesList(uiState.data, onItemClick = onItemClick)
        }

        is UiState.Error -> {
            ErrorScreen(uiState.message)
        }
    }
}

@Composable
fun SourcesList(sources: List<TopHeadlinesSourceResponse.Source>, onItemClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = sources, key = { source -> source.url }) { source ->
            Source(source, onItemClick = onItemClick)
        }
    }
}

@Composable
fun Source(source: TopHeadlinesSourceResponse.Source, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(60.dp),
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = source.name, color = Color.White)
        }
    }
}