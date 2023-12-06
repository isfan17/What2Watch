package com.isfandroid.whattowatch.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isfandroid.whattowatch.core.data.source.local.entity.MultiEntity
import com.isfandroid.whattowatch.core.data.source.local.entity.TrailerEntity
import com.isfandroid.whattowatch.core.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    /** region General **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMulties(multi: List<MultiEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMulti(multi: MultiEntity)

    @Query("SELECT * FROM multi WHERE isTrending = 1")
    fun getTrendingMulties(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE id = :id")
    fun getMultiById(id: Int): Flow<MultiEntity>

    @Query("SELECT * FROM multi WHERE isOnWatchlist = 1")
    fun getOnWatchlistMulti(): Flow<List<MultiEntity>>

    @Query("SELECT EXISTS(SELECT * FROM multi WHERE id = :id AND isNowPlaying = 1)")
    suspend fun isMultiNowPlaying(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM multi WHERE id = :id AND isUpcoming = 1)")
    suspend fun isMultiUpcoming(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM multi WHERE id = :id AND isPopular = 1)")
    suspend fun isMultiPopular(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM multi WHERE id = :id AND isTopRated = 1)")
    suspend fun isMultiTopRated(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM multi WHERE id = :id AND isTrending = 1)")
    suspend fun isMultiTrending(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM multi WHERE id = :id AND isOnWatchlist = 1)")
    suspend fun isMultiOnWatchlist(id: Int): Boolean

    @Query("UPDATE multi SET isOnWatchlist = 1 WHERE id = :id")
    suspend fun setMultiOnWatchlist(id: Int)

    @Query("UPDATE multi SET isOnWatchlist = 0 WHERE id = :id")
    suspend fun removeMultiFromWatchlist(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTrailers(trailers: List<TrailerEntity>)

    @Query("SELECT * FROM trailers WHERE multiId = :id")
    fun getTrailersById(id: Int): Flow<List<TrailerEntity>>
    /** endregion General **/

    /** region Movies **/
    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_MOVIE}' AND isNowPlaying = 1")
    fun getNowPlayingMovies(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_MOVIE}' AND isUpcoming = 1")
    fun getUpcomingMovies(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_MOVIE}' AND isPopular = 1")
    fun getPopularMovies(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_MOVIE}' AND isTopRated = 1")
    fun getTopRatedMovies(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_MOVIE}' AND isOnWatchlist = 1")
    fun getOnWatchlistMovies(): Flow<List<MultiEntity>>
    /** endregion Movies **/

    /** region TV Shows **/
    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_TV_SHOW}' AND isPopular = 1")
    fun getPopularTVShows(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_TV_SHOW}' AND isTopRated = 1")
    fun getTopRatedTVShows(): Flow<List<MultiEntity>>

    @Query("SELECT * FROM multi WHERE mediaType = '${Constants.MEDIA_TYPE_TV_SHOW}' AND isOnWatchlist = 1")
    fun getOnWatchlistTVShows(): Flow<List<MultiEntity>>
    /** endregion TV Shows **/
}