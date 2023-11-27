package com.isfandroid.whattowatch.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.usecase.list.ListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel(private val useCase: ListUseCase): ViewModel() {

    private val _multiLists = MutableStateFlow<PagingData<Multi>?>(null)
    val multiLists: StateFlow<PagingData<Multi>?> = _multiLists

    fun getTrendingThisWeekPaging() {
        viewModelScope.launch {
            useCase.getTrendingThisWeekPaging().collect {
                _multiLists.value = it
            }
        }
    }

    fun getNowPlayingMoviesPaging() {
        viewModelScope.launch {
            useCase.getNowPlayingMoviesPaging().collect {
                _multiLists.value = it
            }
        }
    }

    fun getUpcomingMoviesPaging() {
        viewModelScope.launch {
            useCase.getUpcomingMoviesPaging().collect {
                _multiLists.value = it
            }
        }
    }

    fun getPopularMoviesPaging() {
        viewModelScope.launch {
            useCase.getPopularMoviesPaging().collect {
                _multiLists.value = it
            }
        }
    }

    fun getTopRatedMoviesPaging() {
        viewModelScope.launch {
            useCase.getTopRatedMoviesPaging().collect {
                _multiLists.value = it
            }
        }
    }

    fun getPopularTVShowsPaging() {
        viewModelScope.launch {
            useCase.getPopularTVShowsPaging().collect {
                _multiLists.value = it
            }
        }
    }

    fun getTopRatedTVShowsPaging() {
        viewModelScope.launch {
            useCase.getTopRatedTVShowsPaging().collect {
                _multiLists.value = it
            }
        }
    }
}