package com.raj.newsapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.raj.newsapp.ui.base.ErrorScreen
import com.raj.newsapp.ui.base.Loading
import com.raj.newsapp.ui.base.TopBarScaffold
import com.raj.newsapp.ui.base.UiState
import com.raj.newsapp.viewmodel.NewsByLanguageViewModel

@Composable
fun LanguageNode(
    onItemClick: (language: String) -> Unit = {},
    viewModel: NewsByLanguageViewModel = hiltViewModel()
) {
    val languagesState by viewModel.languagesStateFlow.collectAsStateWithLifecycle()
    TopBarScaffold(title = stringResource(R.string.top_headlines_languages)) {
        LanguagesScreen(languagesState, onItemClick)
    }
}

@Composable
fun NewsByLanguageNode(
    selectedLanguage: String,
    viewModel: NewsByLanguageViewModel = hiltViewModel(),
    onItemClick: (url: String) -> Unit = {},
) {
    viewModel.fetchTopHeadlinesByLanguage(selectedLanguage)
    val languagesState by viewModel.newsByLanguageStateFlow.collectAsStateWithLifecycle()
    TopBarScaffold(title = stringResource(R.string.top_headlines_by_language)) {
        TopHeadlinesScreen(languagesState, onItemClick)
    }
}

@Composable
fun LanguagesScreen(
    uiState: UiState<Map<String, String>>,
    onItemClick: (language: String) -> Unit
) {
    when (uiState) {
        UiState.Loading -> {
            Loading()
        }

        is UiState.Success -> {
            LanguagesList(uiState.data, onItemClick)
        }

        is UiState.Error -> {
            ErrorScreen(uiState.message)
        }
    }
}

@Composable
fun LanguagesList(languagesMap: Map<String, String>, onItemClick: (language: String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = languagesMap.entries.toList(),
            key = { mapEntry -> mapEntry.key }) { mapEntry ->
            Language(mapEntry = mapEntry, onItemClick)
        }
    }
}

@Composable
fun Language(mapEntry: Map.Entry<String, String>, onItemClick: (language: String) -> Unit) {
    Card(modifier = Modifier
        .width(300.dp)
        .height(60.dp)
        .clickable {
            onItemClick(mapEntry.key)
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mapEntry.value,
                color = Color.White
            )
        }
    }
}
