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
import com.raj.newsapp.viewmodel.TopHeadlinesSourcesViewModel


@Composable
fun CountryNode(
    onItemClick: (countryCode: String) -> Unit = {},
    viewModel: TopHeadlinesSourcesViewModel = hiltViewModel()
) {
    viewModel.fetchCountries()
    val countryState by viewModel.countryStateFlow.collectAsStateWithLifecycle()
    TopBarScaffold(title = stringResource(R.string.top_headlines_countries)) {
        CountryScreen(countryState, onItemClick = onItemClick)
    }
}

@Composable
fun NewsByCountry(
    selectedCountry: String,
    onItemClick: (url: String) -> Unit = {},
    viewModel: TopHeadlinesSourcesViewModel = hiltViewModel()
) {
    viewModel.fetchTopHeadlinesByCountry(selectedCountry)
    val topHeadlinesByCountryState by viewModel.topHeadlinesByCountryFlow.collectAsStateWithLifecycle()
    TopBarScaffold(title = stringResource(R.string.top_headlines_by_country)) {
        TopHeadlinesScreen(topHeadlinesByCountryState) { url ->
            onItemClick(url)
        }
    }
}

@Composable
fun CountryScreen(uiState: UiState<Map<String, String>>, onItemClick: (country: String) -> Unit) {
    when (uiState) {
        UiState.Loading -> {
            Loading()
        }

        is UiState.Success -> {
            CountryList(uiState.data, onItemClick)
        }

        is UiState.Error -> {
            ErrorScreen(uiState.message)
        }
    }
}

@Composable
fun CountryList(countryMap: Map<String, String>, onItemClick: (country: String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(countryMap.entries.toList(), key = { map -> map.key }) { map ->
            Country(map, onItemClick)
        }
    }
}

@Composable
fun Country(map: Map.Entry<String, String>, onItemClick: (country: String) -> Unit) {
    Card(modifier = Modifier
        .width(300.dp)
        .height(60.dp)
        .clickable {
            onItemClick(map.key)
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = map.value, color = Color.White)
        }
    }
}