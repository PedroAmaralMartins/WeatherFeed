package com.weatherfeed.app.ui.factory

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.weatherfeed.app.ui.forecast.ForecastViewModel
import com.weatherfeed.app.ui.home.HomeViewModel
import com.weatherfeed.app.ui.search.SearchViewModel
import com.weatherfeed.app.utils.AppContainer

object AppViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            HomeViewModel(AppContainer.repository)
        }
        initializer {
            ForecastViewModel(AppContainer.repository)
        }
        initializer {
            SearchViewModel(AppContainer.repository)
        }
    }
}