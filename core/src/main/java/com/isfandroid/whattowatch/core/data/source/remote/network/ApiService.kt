package com.isfandroid.whattowatch.core.data.source.remote.network

import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiListResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.general.TrailerListResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.movie.MovieDetailResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.tvshow.TVShowDetailResponse
import com.isfandroid.whattowatch.core.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /** region General **/
    @GET("trending/all/week")
    suspend fun getTrendingThisWeek(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = true,
    ): MultiListResponse
    /** endregion General **/

    /** region Movies **/
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MovieDetailResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ): TrailerListResponse
    /** endregion Movies **/

    /** region TV Shows **/
    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTVShows(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ): MultiListResponse

    @GET("tv/{tv_id}")
    suspend fun getTVShowDetail(
        @Path("tv_id") tvShowId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): TVShowDetailResponse

    @GET("tv/{tv_id}/videos")
    suspend fun getTVShowTrailer(
        @Path("tv_id") tvShowId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ): TrailerListResponse
    /** endregion TV Shows **/
}