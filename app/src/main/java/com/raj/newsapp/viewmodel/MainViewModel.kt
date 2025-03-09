package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private var _showTopHeadlines = MutableStateFlow<Boolean>(false)
    internal val showTopHeadlines = _showTopHeadlines.asStateFlow()

    private var _showNewsSource = MutableStateFlow<Boolean>(false)
    internal val showNewsSource = _showNewsSource.asStateFlow()

    private var _showCountries = MutableStateFlow<Boolean>(false)
    internal val showCountries = _showCountries.asStateFlow()

    private var _showLanguage = MutableStateFlow<Boolean>(false)
    internal val showLanguage = _showLanguage.asStateFlow()

    private var _showSearch = MutableStateFlow<Boolean>(false)
    internal val showSearch = _showSearch.asStateFlow()

    private var _showMainScreen = MutableStateFlow(true)
    internal var showMainScreen = _showMainScreen.asStateFlow()


    fun showTopHeadlines() {
        _showTopHeadlines.value = true
        _showNewsSource.value = false
        _showCountries.value = false
        _showLanguage.value = false
        _showSearch.value = false

        _showMainScreen.value = false
    }

    fun showNewsSource() {
        _showTopHeadlines.value = false
        _showNewsSource.value = true
        _showCountries.value = false
        _showLanguage.value = false
        _showSearch.value = false

        _showMainScreen.value = false
    }

    fun showCountries() {
        _showTopHeadlines.value = false
        _showNewsSource.value = false
        _showCountries.value = true
        _showLanguage.value = false
        _showSearch.value = false

        _showMainScreen.value = false

    }

    fun showLanguage() {
        _showTopHeadlines.value = false
        _showNewsSource.value = false
        _showCountries.value = false
        _showLanguage.value = true
        _showSearch.value = false

        _showMainScreen.value = false

    }

    fun showSearch() {
        _showTopHeadlines.value = false
        _showNewsSource.value = false
        _showCountries.value = false
        _showLanguage.value = false
        _showSearch.value = true

        _showMainScreen.value = false
    }

    fun showMainScreen() {
        _showTopHeadlines.value = false
        _showNewsSource.value = false
        _showCountries.value = false
        _showLanguage.value = false
        _showSearch.value = false

        _showMainScreen.value = true
    }

}