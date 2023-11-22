package com.isfandroid.whattowatch.core.domain.usecase.home

import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import kotlinx.coroutines.flow.Flow

interface HomeUseCase {

    fun getTrendingThisWeek(): Flow<Status<List<Multi>>>

    fun getNowPlayingMovies(): Flow<Status<List<Multi>>>
    fun getUpcomingMovies(): Flow<Status<List<Multi>>>
    fun getPopularMovies(): Flow<Status<List<Multi>>>
    fun getTopRatedMovies(): Flow<Status<List<Multi>>>

    fun getPopularTVShows(): Flow<Status<List<Multi>>>
    fun getTopRatedTVShows(): Flow<Status<List<Multi>>>
}