package com.isfandroid.whattowatch.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.usecase.home.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val useCase: HomeUseCase): ViewModel() {

    // GENERAL
    private val _trendingMulti = MutableStateFlow<Status<List<Multi>>?>(null)
    val trendingMulti: StateFlow<Status<List<Multi>>?> = _trendingMulti

    // MOVIES
    private val _nowPlayingMovies = MutableStateFlow<Status<List<Multi>>?>(null)
    val nowPlayingMovies: StateFlow<Status<List<Multi>>?> = _nowPlayingMovies

    private val _upcomingMovies = MutableStateFlow<Status<List<Multi>>?>(null)
    val upcomingMovies: StateFlow<Status<List<Multi>>?> = _upcomingMovies

    private val _popularMovies = MutableStateFlow<Status<List<Multi>>?>(null)
    val popularMovies: StateFlow<Status<List<Multi>>?> = _popularMovies

    private val _topRatedMovies = MutableStateFlow<Status<List<Multi>>?>(null)
    val topRatedMovies: StateFlow<Status<List<Multi>>?> = _topRatedMovies

    // TV SHOWS
    private val _popularTVShows = MutableStateFlow<Status<List<Multi>>?>(null)
    val popularTVShows: StateFlow<Status<List<Multi>>?> = _popularTVShows

    private val _topRatedTVShows = MutableStateFlow<Status<List<Multi>>?>(null)
    val topRatedTVShows: StateFlow<Status<List<Multi>>?> = _topRatedTVShows

    // Error State
    private val _hasError = MutableStateFlow<Boolean?>(null)
    val hasError: StateFlow<Boolean?> = _hasError

    fun getTrendingMulti() {
        viewModelScope.launch {
            useCase.getTrendingThisWeek().collect {
                _trendingMulti.value = it
            }
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            useCase.getNowPlayingMovies().collect {
                _nowPlayingMovies.value = it
            }
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch {
            useCase.getUpcomingMovies().collect {
                _upcomingMovies.value = it
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            useCase.getPopularMovies().collect {
                _popularMovies.value = it
            }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            useCase.getTopRatedMovies().collect {
                _topRatedMovies.value = it
            }
        }
    }

    fun getPopularTVShows() {
        viewModelScope.launch {
            useCase.getPopularTVShows().collect {
                _popularTVShows.value = it
            }
        }
    }

    fun getTopRatedTVShows() {
        viewModelScope.launch {
            useCase.getTopRatedTVShows().collect {
                _topRatedTVShows.value = it
            }
        }
    }

    fun checkForErrors() {
        _hasError.value = listOf(
            _trendingMulti,
            _nowPlayingMovies,
            _upcomingMovies,
            _popularMovies,
            _topRatedMovies,
            _popularTVShows,
            _topRatedTVShows
        ).any { it.value is Status.Error }
    }
}