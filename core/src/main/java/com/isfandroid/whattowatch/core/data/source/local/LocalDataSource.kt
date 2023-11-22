package com.isfandroid.whattowatch.core.data.source.local

import com.isfandroid.whattowatch.core.data.source.local.entity.MultiEntity
import com.isfandroid.whattowatch.core.data.source.local.entity.TrailerEntity
import com.isfandroid.whattowatch.core.data.source.local.room.AppDao

class LocalDataSource(private val appDao: AppDao) {

    /** region General **/
    suspend fun upsertMulties(multies: List<MultiEntity>) = appDao.upsertMulties(multies)
    suspend fun upsertMulti(multi: MultiEntity) = appDao.upsertMulti(multi)
    fun getTrendingMulti() = appDao.getTrendingMulties()
    fun getOnWatchlistMulti() = appDao.getOnWatchlistMulti()
    fun getMultiById(id: Int) = appDao.getMultiById(id)
    suspend fun isMultiNowPlaying(id: Int) = appDao.isMultiNowPlaying(id)
    suspend fun isMultiUpcoming(id: Int) = appDao.isMultiUpcoming(id)
    suspend fun isMultiPopular(id: Int) = appDao.isMultiPopular(id)
    suspend fun isMultiTopRated(id: Int) = appDao.isMultiTopRated(id)
    suspend fun isMultiTrending(id: Int) = appDao.isMultiTrending(id)
    suspend fun isMultiOnWatchlist(id: Int) = appDao.isMultiOnWatchlist(id)
    suspend fun setMultiOnWatchlist(id: Int) = appDao.setMultiOnWatchlist(id)
    suspend fun removeMultiFromWatchlist(id: Int) = appDao.removeMultiFromWatchlist(id)
    suspend fun upsertTrailers(trailers: List<TrailerEntity>) = appDao.upsertTrailers(trailers)
    fun getTrailersById(id: Int) = appDao.getTrailersById(id)
    /** endregion General **/

    /** region Movies **/
    fun getNowPlayingMovies() = appDao.getNowPlayingMovies()
    fun getUpcomingMovies() = appDao.getUpcomingMovies()
    fun getPopularMovies() = appDao.getPopularMovies()
    fun getTopRatedMovies() = appDao.getTopRatedMovies()
    fun getOnWatchlistMovies() = appDao.getOnWatchlistMovies()
    /** endregion Movies **/

    /** region TV Shows **/
    fun getPopularTVShows() = appDao.getPopularTVShows()
    fun getTopRatedTVShows() = appDao.getTopRatedTVShows()
    fun getOnWatchlistTVShows() = appDao.getOnWatchlistTVShows()
    /** endregion TV Shows **/

}