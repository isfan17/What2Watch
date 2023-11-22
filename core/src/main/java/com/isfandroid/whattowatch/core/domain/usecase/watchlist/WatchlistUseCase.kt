package com.isfandroid.whattowatch.core.domain.usecase.watchlist

import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import kotlinx.coroutines.flow.Flow

interface WatchlistUseCase {
    fun getOnWatchlistMulti(): Flow<Status<List<Multi>>>
    fun getOnWatchlistMovies(): Flow<Status<List<Multi>>>
    fun getOnWatchlistTVShows(): Flow<Status<List<Multi>>>
}