package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.newsapp.common.DispatcherProvider
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.data.TopHeadlinesSourcesResponse
import com.raj.newsapp.model.repository.NewsRepository
import com.raj.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesSourcesViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _sourcesStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesSourcesResponse.Source>>>(UiState.Loading)
    internal val sourcesStateFlow = _sourcesStateFlow.asStateFlow()

    private val _topHeadlinesBySourceStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesResponse.Article>>>(UiState.Loading)
    internal val topHeadlinesBySourceStateFlow = _topHeadlinesBySourceStateFlow.asStateFlow()

    init {
        fetchTopHeadlinesSources()
    }

    private fun fetchTopHeadlinesSources() {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchNewsSources()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _sourcesStateFlow.value = UiState.Error(e.toString())
                }.collect { topHeadlinesSources ->
                    when (topHeadlinesSources.status) {
                        "ok" -> {
                            _sourcesStateFlow.value = UiState.Success(topHeadlinesSources.sources)
                        }

                        else -> {
                            _sourcesStateFlow.value =
                                UiState.Error(message = "Something went wrong")
                        }
                    }
                }
        }
    }

    internal fun fetchTopHeadlinesBySource(source: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchTopHeadlinesBySource(source)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _topHeadlinesBySourceStateFlow.value = UiState.Error(e.toString())
                }.collect { topHeadlinesResponse ->
                    when (topHeadlinesResponse.status) {
                        "ok" -> {
                            if (topHeadlinesResponse.articles.isNotEmpty()) {
                                _topHeadlinesBySourceStateFlow.value =
                                    UiState.Success(topHeadlinesResponse.articles)
                            } else {
                                _topHeadlinesBySourceStateFlow.value =
                                    UiState.Error(message = "Currently no article for selected sources. Please select another source")
                            }
                        }

                        else -> {
                            _topHeadlinesBySourceStateFlow.value =
                                UiState.Error(message = "Something went wrong")
                        }
                    }
                }
        }
    }
}