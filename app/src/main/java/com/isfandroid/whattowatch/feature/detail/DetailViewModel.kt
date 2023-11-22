package com.isfandroid.whattowatch.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.model.Trailer
import com.isfandroid.whattowatch.core.domain.usecase.detail.DetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: DetailUseCase): ViewModel() {

    private val _multiDetail = MutableStateFlow<Status<Multi>?>(null)
    val multiDetail: StateFlow<Status<Multi>?> = _multiDetail

    private val _trailers = MutableStateFlow<Status<List<Trailer>>?>(null)
    val trailers: StateFlow<Status<List<Trailer>>?> = _trailers

    private val _watchlistActionResult = MutableStateFlow<String?>(null)
    val watchlistActionResult: StateFlow<String?> = _watchlistActionResult

    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            useCase.getMovieDetail(movieId).collect {
                _multiDetail.value = it
            }
        }
    }

    fun getMovieTrailers(movieId: Int) {
        viewModelScope.launch {
            useCase.getMovieTrailers(movieId).collect {
                _trailers.value = it
            }
        }
    }

    fun getTVShowDetail(tvId: Int) {
        viewModelScope.launch {
            useCase.getTVShowDetail(tvId).collect {
                _multiDetail.value = it
            }
        }
    }

    fun getTVShowTrailers(tvId: Int) {
        viewModelScope.launch {
            useCase.getTVShowTrailers(tvId).collect {
                _trailers.value = it
            }
        }
    }

    fun addMultiToWatchlist(multiId: Int, mediaType: String) {
        viewModelScope.launch {
            useCase.setMultiToWatchlist(multiId, mediaType).collect {
                _watchlistActionResult.value = it
            }
        }
    }

    fun removeMultiFromWatchlist(multiId: Int, mediaType: String) {
        viewModelScope.launch {
            useCase.removeMultiFromWatchlist(multiId, mediaType).collect {
                _watchlistActionResult.value = it
            }
        }
    }
}