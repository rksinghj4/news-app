package com.raj.newsapp.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.newsapp.R
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.ui.base.ErrorScreen
import com.raj.newsapp.ui.base.Loading
import com.raj.newsapp.ui.base.TopBarScaffold
import com.raj.newsapp.ui.base.UiState
import com.raj.newsapp.ui.base.navigations.ArticleList
import com.raj.newsapp.viewmodel.TopHeadlineViewModel

@Composable
fun TopHeadlinesNode(
    onNewsClick: (url: String) -> Unit,
    topHeadlineViewModel: TopHeadlineViewModel = hiltViewModel()
) {
    val uiSate by topHeadlineViewModel.uiStateFlow.collectAsStateWithLifecycle()

    TopBarScaffold(title = stringResource(R.string.top_headlines)) {
        TopHeadlinesScreen(uiSate) {url ->
            onNewsClick(url)
        }
    }
}

@Composable
fun TopHeadlinesScreen(
    uiState: UiState<List<TopHeadlinesResponse.Article>>,
    onNewsClick: (url: String) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> {//is is optional with object
            Loading()
        }

        is UiState.Success -> {
            ArticleList(uiState.data) { url ->
                onNewsClick(url)
            }
        }

        is UiState.Error -> {//Use is with data class
            ErrorScreen(uiState.message)
        }
    }
}



