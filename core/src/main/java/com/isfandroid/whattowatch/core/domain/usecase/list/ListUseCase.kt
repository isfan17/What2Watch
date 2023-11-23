package com.isfandroid.whattowatch.core.domain.usecase.list

import androidx.paging.PagingData
import com.isfandroid.whattowatch.core.domain.model.Multi
import kotlinx.coroutines.flow.Flow

interface ListUseCase {

    fun getTrendingThisWeekPaging(): Flow<PagingData<Multi>>

    fun getNowPlayingMoviesPaging(): Flow<PagingData<Multi>>
    fun getUpcomingMoviesPaging(): Flow<PagingData<Multi>>
    fun getPopularMoviesPaging(): Flow<PagingData<Multi>>
    fun getTopRatedMoviesPaging(): Flow<PagingData<Multi>>

    fun getPopularTVShowsPaging(): Flow<PagingData<Multi>>
    fun getTopRatedTVShowsPaging(): Flow<PagingData<Multi>>

}