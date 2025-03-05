package com.raj.newsapp.viewmodel

import androidx.lifecycle.ViewModel

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
class NewsViewModel: ViewModel() {

}