package com.isfandroid.whattowatch.core.domain.usecase.list

import com.isfandroid.whattowatch.core.domain.repository.IAppRepository

class ListInteractor(private val repository: IAppRepository): ListUseCase {

    override fun getTrendingThisWeekPaging() = repository.getTrendingThisWeekPaging()

    override fun getNowPlayingMoviesPaging() = repository.getNowPlayingMoviesPaging()
    override fun getUpcomingMoviesPaging() = repository.getUpcomingMoviesPaging()
    override fun getPopularMoviesPaging() = repository.getPopularMoviesPaging()
    override fun getTopRatedMoviesPaging() = repository.getTopRatedMoviesPaging()

    override fun getPopularTVShowsPaging() = repository.getPopularTVShowsPaging()
    override fun getTopRatedTVShowsPaging() = repository.getTopRatedTVShowsPaging()
}