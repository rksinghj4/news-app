package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.newsapp.common.DispatcherProvider
import com.raj.newsapp.model.data.TopHeadlinesResponse
import com.raj.newsapp.model.repository.NewsRepository
import com.raj.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsBySearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _searchResultStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesResponse.Article>>>(UiState.Loading)
    internal val searchResultStateFlow = _searchResultStateFlow.asStateFlow()

    private val _searchQueryStateFlow = MutableStateFlow("")
    internal val searchQueryStateFlow = _searchQueryStateFlow.asStateFlow()

    init {
        searchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    internal fun searchQuery() {
        viewModelScope.launch {
            searchQueryStateFlow
                .debounce(300)
                .filter { query ->
                    if (query.isNotEmpty()) {
                        return@filter true
                    } else {
                        return@filter false
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { nonEmptyQuery ->
                    flow { emit(fetchNewsBySearchQuery(nonEmptyQuery)) }
                }.flowOn(dispatcherProvider.default).collect()
        }
    }

    private fun fetchNewsBySearchQuery(searchQueries: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchNewsBySearchQuery(searchQueries)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _searchResultStateFlow.value = UiState.Error(e.toString())
                }.collect { topHeadlinesResponse ->
                    when (topHeadlinesResponse.status) {
                        "ok" -> {
                            if (topHeadlinesResponse.articles.isNotEmpty()) {
                                _searchResultStateFlow.value =
                                    UiState.Success(topHeadlinesResponse.articles)
                            } else {
                                _searchResultStateFlow.value =
                                    UiState.Error("Currently no article for this keyword. Search something different")
                            }
                        }

                        else -> {
                            _searchResultStateFlow.value = UiState.Error("Something went wrong")
                        }
                    }

                }
        }
    }

    internal fun updateQuery(updatedQuery: String) {
        _searchQueryStateFlow.value = updatedQuery
    }
}