package com.isfandroid.whattowatch.core.domain.usecase.home

import com.isfandroid.whattowatch.core.domain.repository.IAppRepository

class HomeInteractor(private val repository: IAppRepository): HomeUseCase {

    override fun getTrendingThisWeek() = repository.getTrendingThisWeek()

    override fun getNowPlayingMovies() = repository.getNowPlayingMovies()
    override fun getUpcomingMovies() = repository.getUpcomingMovies()
    override fun getPopularMovies() = repository.getPopularMovies()
    override fun getTopRatedMovies() = repository.getTopRatedMovies()

    override fun getPopularTVShows() = repository.getPopularTVShows()
    override fun getTopRatedTVShows() = repository.getTopRatedTVShows()
}