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
class NewsByCountryViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _countryStateFlow =
        MutableStateFlow<UiState<Map<String, String>>>(UiState.Loading)
    internal val countryStateFlow = _countryStateFlow.asStateFlow()


    private val _topHeadlinesByCountryStateFlow =
        MutableStateFlow<UiState<List<TopHeadlinesResponse.Article>>>(UiState.Loading)
    internal val topHeadlinesByCountryStateFlow = _topHeadlinesByCountryStateFlow.asStateFlow()


    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        val countryMap = mutableMapOf<String, String>()
        countryMap["ae"] = "United Arab Emirates"
        countryMap["ar"] = "Argentina"
        countryMap["at"] = "Austria"
        countryMap["au"] = "Australia"
        countryMap["be"] = "Belgium"
        countryMap["bg"] = "Bulgaria"
        countryMap["br"] = "Brazil"
        countryMap["ca"] = "Canada"
        countryMap["ch"] = "Switzerland"
        countryMap["cn"] = "China"
        countryMap["co"] = "Colombia"
        countryMap["cu"] = "Cuba"
        countryMap["cz"] = "Czech Republic"
        countryMap["de"] = "Germany"
        countryMap["eg"] = "Egypt"
        countryMap["fr"] = "France"
        countryMap["gb"] = "United Kingdom"
        countryMap["gr"] = "Greece"
        countryMap["hk"] = "Hong Kong"
        countryMap["hu"] = "Hungary"
        countryMap["id"] = "Indonesia"
        countryMap["ie"] = "Ireland"
        countryMap["il"] = "Israel"
        countryMap["in"] = "India"
        countryMap["it"] = "Italy"
        countryMap["jp"] = "Japan"
        countryMap["kr"] = "South Korea"
        countryMap["lt"] = "Lithuania"
        countryMap["lv"] = "Latvia"
        countryMap["ma"] = "Morocco"
        countryMap["mx"] = "Mexico"
        countryMap["my"] = "Malaysia"
        countryMap["ng"] = "Nigeria"
        countryMap["nl"] = "Netherlands"
        countryMap["no"] = "Norway"
        countryMap["nz"] = "New Zealand"
        countryMap["ph"] = "Philippines"
        countryMap["pl"] = "Poland"
        countryMap["pt"] = "Portugal"
        countryMap["ro"] = "Romania"
        countryMap["rs"] = "Serbia"
        countryMap["ru"] = "Russia"
        countryMap["sa"] = "Saudi Arabia"
        countryMap["se"] = "Sweden"
        countryMap["sg"] = "Singapore"
        countryMap["si"] = "Slovenia"
        countryMap["sk"] = "Slovakia"
        countryMap["th"] = "Thailand"
        countryMap["tr"] = "Turkey"
        countryMap["tw"] = "Taiwan"
        countryMap["ua"] = "Ukraine"
        countryMap["us"] = "United States"
        countryMap["ve"] = "Venezuela"
        countryMap["za"] = "South Africa"

        _countryStateFlow.value = UiState.Success(data = countryMap)
    }

    internal fun fetchTopHeadlinesByCountry(countryCode: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            newsRepository.fetchTopHeadlinesByCountry(countryCode)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _topHeadlinesByCountryStateFlow.value = UiState.Error(e.toString())
                }.collect { topHeadlinesResponse ->
                    when (topHeadlinesResponse.status) {
                        "ok" -> {
                            if (topHeadlinesResponse.articles.isNotEmpty()) {
                                _topHeadlinesByCountryStateFlow.value =
                                    UiState.Success(topHeadlinesResponse.articles)
                            } else {
                                _topHeadlinesByCountryStateFlow.value =
                                    UiState.Error(message = "Currently no article for selected country. Please select another country")
                            }
                        }

                        else -> {
                            _topHeadlinesByCountryStateFlow.value =
                                UiState.Error(message = "Something went wrong")
                        }
                    }
                }
        }
    }
}