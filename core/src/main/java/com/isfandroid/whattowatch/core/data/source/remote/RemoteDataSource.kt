package com.isfandroid.whattowatch.core.data.source.remote

import com.isfandroid.whattowatch.core.data.source.remote.network.ApiResponse
import com.isfandroid.whattowatch.core.data.source.remote.network.ApiService
import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiListResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.general.TrailerListResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.movie.MovieDetailResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.tvshow.TVShowDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.Exception

class RemoteDataSource (private val apiService: ApiService) {

    /** region General **/
    suspend fun getTrendingThisWeek(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getTrendingThisWeek(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getTrendingThisWeekPaging(page: Int) = apiService.getTrendingThisWeek(page = page)

    suspend fun searchMulti(
        page: Int,
        query: String,
    ) = apiService.searchMulti(page = page, query = query)
    /** endregion General **/

    /** region Movies **/
    suspend fun getNowPlayingMovies(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getNowPlayingMovies(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getUpcomingMovies(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getUpcomingMovies(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPopularMovies(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getPopularMovies(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getTopRatedMovies(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getTopRatedMovies(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getNowPlayingMoviesPaging(page: Int) = apiService.getNowPlayingMovies(page = page)
    suspend fun getUpcomingMoviesPaging(page: Int) = apiService.getUpcomingMovies(page = page)
    suspend fun getPopularMoviesPaging(page: Int) = apiService.getPopularMovies(page = page)
    suspend fun getTopRatedMoviesPaging(page: Int) = apiService.getTopRatedMovies(page = page)

    suspend fun getMovieDetail(
        movieId: Int,
    ): Flow<ApiResponse<MovieDetailResponse>> = flow {
        try {
            val response = apiService.getMovieDetail(movieId = movieId)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getMovieTrailer(
        movieId: Int,
    ): Flow<ApiResponse<TrailerListResponse>> = flow {
        try {
            val response = apiService.getMovieTrailer(movieId = movieId)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
    /** endregion Movies **/

    /** region TV Shows **/
    suspend fun getPopularTVShows(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getPopularTVShows(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getTopRatedTVShows(): Flow<ApiResponse<MultiListResponse>> = flow {
        try {
            val response = apiService.getTopRatedTVShows(page = 1)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPopularTVShowsPaging(page: Int) = apiService.getPopularTVShows(page = page)
    suspend fun getTopRatedTVShowsPaging(page: Int) = apiService.getTopRatedTVShows(page = page)

    suspend fun getTVShowDetail(
        tvShowId: Int,
    ): Flow<ApiResponse<TVShowDetailResponse>> = flow {
        try {
            val response = apiService.getTVShowDetail(tvShowId = tvShowId)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getTVShowTrailer(
        tvShowId: Int,
    ): Flow<ApiResponse<TrailerListResponse>> = flow {
        try {
            val response = apiService.getTVShowTrailer(tvShowId = tvShowId)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
    /** endregion TV Shows **/
}