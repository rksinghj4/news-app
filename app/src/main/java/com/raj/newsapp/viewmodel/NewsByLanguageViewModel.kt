package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class NewsByLanguageViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _languagesStateFlow =
        MutableStateFlow<UiState<Map<String, String>>>(UiState.Loading)
    internal val languagesStateFlow = _languagesStateFlow.asStateFlow()

    private val _newsByLanguageStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesResponse.Article>>>(UiState.Loading)
    internal val newsByLanguageStateFlow = _newsByLanguageStateFlow.asStateFlow()

    init {
        fetchLanguage()
    }

    private fun fetchLanguage() {
        val languageMap = mapOf(
            "ar" to "Arabic",
            "de" to "German",
            "en" to "English",
            "es" to "Spanish",
            "fr" to "French",
            "he" to "Hebrew",
            "it" to "Italian",
            "nl" to "Dutch",
            "no" to "Norwegian",
            "pt" to "Portuguese",
            "ru" to "Russian",
            "sv" to "Swedish",
            "ud" to "Udmurt",
            "zh" to "Chinese"
        )
        _languagesStateFlow.value = UiState.Success(languageMap)
    }

    internal fun fetchTopHeadlinesByLanguage(language: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchTopHeadlinesByLanguage(language)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsByLanguageStateFlow.value = UiState.Error(message = e.toString())
                }.collect { topHeadlinesResponse ->
                    when (topHeadlinesResponse.status) {
                        "ok" -> {
                            if (topHeadlinesResponse.articles.isNotEmpty()) {
                                _newsByLanguageStateFlow.value =
                                    UiState.Success(topHeadlinesResponse.articles)
                            } else {
                                UiState.Error(message = "Currently no article for selected language. Please select another language")
                            }
                        }

                        else -> {
                            _newsByLanguageStateFlow.value =
                                UiState.Error(message = "Something went wrong")
                        }
                    }

                }
        }
    }
}