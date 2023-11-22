package com.isfandroid.whattowatch.core.domain.usecase.detail

import com.isfandroid.whattowatch.core.domain.repository.IAppRepository

class DetailInteractor(private val repository: IAppRepository): DetailUseCase {
    override fun getMovieDetail(movieId: Int) = repository.getMovieDetail(movieId)
    override fun getMovieTrailers(movieId: Int) = repository.getMovieTrailers(movieId)

    override fun getTVShowDetail(tvId: Int) = repository.getTVShowDetail(tvId)
    override fun getTVShowTrailers(tvId: Int) = repository.getTVShowTrailers(tvId)

    override fun setMultiToWatchlist(multiId: Int, mediaType: String)
    = repository.setMultiOnWatchlist(multiId, mediaType)
    override fun removeMultiFromWatchlist(multiId: Int, mediaType: String)
    = repository.removeMultiFromWatchlist(multiId, mediaType)
}