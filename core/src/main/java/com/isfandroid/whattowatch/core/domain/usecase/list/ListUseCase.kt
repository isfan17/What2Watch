package com.isfandroid.whattowatch.core.domain.usecase.list

import androidx.paging.PagingData
import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiResponse
import kotlinx.coroutines.flow.Flow

interface ListUseCase {

    fun getTrendingThisWeekPaging(): Flow<PagingData<MultiResponse>>

    fun getNowPlayingMoviesPaging(): Flow<PagingData<MultiResponse>>
    fun getUpcomingMoviesPaging(): Flow<PagingData<MultiResponse>>
    fun getPopularMoviesPaging(): Flow<PagingData<MultiResponse>>
    fun getTopRatedMoviesPaging(): Flow<PagingData<MultiResponse>>

    fun getPopularTVShowsPaging(): Flow<PagingData<MultiResponse>>
    fun getTopRatedTVShowsPaging(): Flow<PagingData<MultiResponse>>

}