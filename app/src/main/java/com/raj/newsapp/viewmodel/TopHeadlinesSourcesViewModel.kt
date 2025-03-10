package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.newsapp.common.DispatcherProvider
import com.raj.newsapp.model.data.TopHeadlinesSourceResponse
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

    private val _sourceStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesSourceResponse.Source>>>(UiState.Loading)

    internal val sourceStateFlow = _sourceStateFlow.asStateFlow()

    init {
        fetchTopHeadlinesSources()
    }

    private fun fetchTopHeadlinesSources() {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchNewsSources()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _sourceStateFlow.value = UiState.Error(e.toString())
                }.collect { topHeadlinesSource ->
                    when (topHeadlinesSource.status) {
                        "ok" -> {
                            _sourceStateFlow.value = UiState.Success(topHeadlinesSource.sources)
                        }

                        else -> {
                            _sourceStateFlow.value = UiState.Error(message = "Something went wrong")
                        }
                    }
                }
        }
    }
}