package com.isfandroid.whattowatch.core.domain.usecase.watchlist

import com.isfandroid.whattowatch.core.domain.repository.IAppRepository

class WatchlistInteractor(private val repository: IAppRepository): WatchlistUseCase {
    override fun getOnWatchlistMulti() = repository.getOnWatchlistMulti()
    override fun getOnWatchlistMovies() = repository.getOnWatchlistMovies()
    override fun getOnWatchlistTVShows() = repository.getOnWatchlistTVShows()
}