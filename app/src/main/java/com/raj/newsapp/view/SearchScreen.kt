package com.raj.newsapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.newsapp.R
import com.raj.newsapp.ui.base.TopBarScaffold
import com.raj.newsapp.viewmodel.NewsBySearchViewModel

@Composable
fun SearchScreenNode(
    onItemClick: (url: String) -> Unit
) {
    TopBarScaffold(title = stringResource(R.string.top_headlines_by_search)) {
        SearchScreen(onItemClick = onItemClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: NewsBySearchViewModel = hiltViewModel(),
    onItemClick: (url: String) -> Unit
) {
    val searchQueryState by viewModel.searchQueryStateFlow.collectAsStateWithLifecycle()
    val searchResultState by viewModel.searchResultStateFlow.collectAsStateWithLifecycle()
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = searchQueryState,
        onQueryChange = { query ->
            viewModel.updateQuery(query)
        },
        onSearch = {
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        modifier = Modifier
            .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),

        placeholder = { Text("Search") },

        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        viewModel.updateQuery("")
                    },
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        tonalElevation = 0.dp,
    ) {
        TopHeadlinesScreen(searchResultState) { url ->
            onItemClick(url)
        }
    }
}
