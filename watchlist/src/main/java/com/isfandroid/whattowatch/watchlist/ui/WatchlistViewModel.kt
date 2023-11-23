package com.isfandroid.whattowatch.watchlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.usecase.watchlist.WatchlistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel(private val useCase: WatchlistUseCase): ViewModel() {

    private val _watchLists = MutableStateFlow<Status<List<Multi>>?>(null)
    val watchLists: StateFlow<Status<List<Multi>>?> = _watchLists

    fun getOnWatchlistMulti() {
        viewModelScope.launch {
            useCase.getOnWatchlistMulti().collect {
                _watchLists.value = it
            }
        }
    }

    fun getOnWatchlistMovies() {
        viewModelScope.launch {
            useCase.getOnWatchlistMovies().collect {
                _watchLists.value = it
            }
        }
    }

    fun getOnWatchlistTVShows() {
        viewModelScope.launch {
            useCase.getOnWatchlistTVShows().collect {
                _watchLists.value = it
            }
        }
    }
}