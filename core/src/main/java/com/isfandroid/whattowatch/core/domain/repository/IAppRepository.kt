package com.isfandroid.whattowatch.core.domain.repository

import androidx.paging.PagingData
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.model.Trailer
import kotlinx.coroutines.flow.Flow

interface IAppRepository {

    fun getTrendingThisWeek(): Flow<Status<List<Multi>>>
    fun getTrendingThisWeekPaging(): Flow<PagingData<Multi>>
    fun getOnWatchlistMulti(): Flow<Status<List<Multi>>>
    fun searchMultiPaging(query: String): Flow<PagingData<Multi>>
    fun setMultiOnWatchlist(id: Int, mediaType: String): Flow<String>
    fun removeMultiFromWatchlist(id: Int, mediaType: String): Flow<String>

    fun getNowPlayingMovies(): Flow<Status<List<Multi>>>
    fun getUpcomingMovies(): Flow<Status<List<Multi>>>
    fun getPopularMovies(): Flow<Status<List<Multi>>>
    fun getTopRatedMovies(): Flow<Status<List<Multi>>>
    fun getOnWatchlistMovies(): Flow<Status<List<Multi>>>
    fun getMovieDetail(movieId: Int): Flow<Status<Multi>>
    fun getMovieTrailers(movieId: Int): Flow<Status<List<Trailer>>>

    fun getNowPlayingMoviesPaging(): Flow<PagingData<Multi>>
    fun getUpcomingMoviesPaging(): Flow<PagingData<Multi>>
    fun getPopularMoviesPaging(): Flow<PagingData<Multi>>
    fun getTopRatedMoviesPaging(): Flow<PagingData<Multi>>

    fun getPopularTVShows(): Flow<Status<List<Multi>>>
    fun getTopRatedTVShows(): Flow<Status<List<Multi>>>
    fun getOnWatchlistTVShows(): Flow<Status<List<Multi>>>
    fun getTVShowDetail(tvId: Int): Flow<Status<Multi>>
    fun getTVShowTrailers(tvId: Int): Flow<Status<List<Trailer>>>

    fun getPopularTVShowsPaging(): Flow<PagingData<Multi>>
    fun getTopRatedTVShowsPaging(): Flow<PagingData<Multi>>
}