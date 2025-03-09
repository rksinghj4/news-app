package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.newsapp.common.Constants.COUNTRY
import com.raj.newsapp.common.DispatcherProvider
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.repository.NewsRepository
import com.raj.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel is a class which act as bridge between UI and Model layers.
 * ViewModel survive configurations changes and help us in persisting the data during config changes.
 * It avoids unnecessary network call or data fetch operation during config changes.
 * It is lifecycle aware and gets destroyed only when owner gets destroyed completely.
 *
 * Exposes observable data to UI layer
 * Completely dumb about UI, no idea who is going to use me(No direct reference).
 * Mostly business logic is part of Model layers but some times
 * we may need to write it in viewmodel also
 * e.g. Fetching data from multiple repository and then
 * 1. merging the data and displaying to user
 * 2. Filtering the common data then displaying to user.
 * UI logic and event handling logic resides in ViewModel.
 *
 * SavedInstanceState can be used from small and simple data
 * whereas viewmodel can be used for complex and comparatively large data.
 */
@HiltViewModel
class TopHeadlineViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesResponse.Article>>>(UiState.Loading)
    internal val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchTopHeadlines(COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiStateFlow.value = UiState.Error(e.toString())
                }.collect { topHeadlinesResponse ->
                    when (topHeadlinesResponse.status) {
                        "ok" -> {
                            _uiStateFlow.value =
                                UiState.Success(data = topHeadlinesResponse.articles)
                        }
                        else -> {
                            _uiStateFlow.value = UiState.Error(message = "Something went wrong")
                        }
                    }
                }
        }
    }
}