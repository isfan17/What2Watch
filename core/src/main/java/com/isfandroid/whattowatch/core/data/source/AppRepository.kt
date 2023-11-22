package com.isfandroid.whattowatch.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.data.source.local.LocalDataSource
import com.isfandroid.whattowatch.core.data.source.paging.MultiPagingSource
import com.isfandroid.whattowatch.core.data.source.remote.RemoteDataSource
import com.isfandroid.whattowatch.core.data.source.remote.network.ApiResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiResponse
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.model.Trailer
import com.isfandroid.whattowatch.core.domain.repository.IAppRepository
import com.isfandroid.whattowatch.core.utils.Constants
import com.isfandroid.whattowatch.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): IAppRepository {

    /** region General **/
    override fun getTrendingThisWeek(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getTrendingMulti().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getTrendingThisWeek().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.isTrending = true

                    it.isNowPlaying = localDataSource.isMultiNowPlaying(it.id)
                    it.isUpcoming = localDataSource.isMultiUpcoming(it.id)
                    it.isPopular = localDataSource.isMultiPopular(it.id)
                    it.isTopRated = localDataSource.isMultiTopRated(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getTrendingThisWeek : Response is Empty"))
            }
        }
    }

    override fun getTrendingThisWeekPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.TRENDING_MULTI,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getOnWatchlistMulti(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getOnWatchlistMulti().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))
    }

    override fun searchMultiPaging(query: String): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.SEARCH_MULTI,
                    remoteDataSource,
                    query,
                )
            }
        ).flow
    }

    override fun setMultiOnWatchlist(id: Int, mediaType: String): Flow<String> = flow {
        localDataSource.setMultiOnWatchlist(id)
        if (mediaType == Constants.MEDIA_TYPE_MOVIE) {
            emit("Movie Successfully Added to Watchlist")
        } else {
            emit("TV Show Successfully Added to Watchlist")
        }
    }

    override fun removeMultiFromWatchlist(id: Int, mediaType: String): Flow<String> = flow {
        localDataSource.removeMultiFromWatchlist(id)
        if (mediaType == Constants.MEDIA_TYPE_MOVIE) {
            emit("Movie Successfully Removed from Watchlist")
        } else {
            emit("TV Show Successfully Removed from Watchlist")
        }
    }
    /** endregion General **/

    /** region Movies **/
    override fun getNowPlayingMovies(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getNowPlayingMovies().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getNowPlayingMovies().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.mediaType = Constants.MEDIA_TYPE_MOVIE
                    it.isNowPlaying = true

                    it.isTrending = localDataSource.isMultiTrending(it.id)
                    it.isUpcoming = localDataSource.isMultiUpcoming(it.id)
                    it.isPopular = localDataSource.isMultiPopular(it.id)
                    it.isTopRated = localDataSource.isMultiTopRated(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getNowPlayingMovies : Response is Empty"))
            }
        }
     }

    override fun getUpcomingMovies(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getUpcomingMovies().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getUpcomingMovies().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.mediaType = Constants.MEDIA_TYPE_MOVIE
                    it.isUpcoming = true

                    it.isTrending = localDataSource.isMultiTrending(it.id)
                    it.isNowPlaying = localDataSource.isMultiNowPlaying(it.id)
                    it.isPopular = localDataSource.isMultiPopular(it.id)
                    it.isTopRated = localDataSource.isMultiTopRated(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getUpcomingMovies : Response is Empty"))
            }
        }
    }

    override fun getPopularMovies(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getPopularMovies().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getPopularMovies().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.mediaType = Constants.MEDIA_TYPE_MOVIE
                    it.isPopular = true

                    it.isTrending = localDataSource.isMultiTrending(it.id)
                    it.isUpcoming = localDataSource.isMultiUpcoming(it.id)
                    it.isNowPlaying = localDataSource.isMultiNowPlaying(it.id)
                    it.isTopRated = localDataSource.isMultiTopRated(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getPopularMovies : Response is Empty"))
            }
        }
    }

    override fun getTopRatedMovies(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getTopRatedMovies().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getTopRatedMovies().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.mediaType = Constants.MEDIA_TYPE_MOVIE
                    it.isTopRated = true

                    it.isTrending = localDataSource.isMultiTrending(it.id)
                    it.isUpcoming = localDataSource.isMultiUpcoming(it.id)
                    it.isPopular = localDataSource.isMultiPopular(it.id)
                    it.isNowPlaying = localDataSource.isMultiNowPlaying(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getTopRatedMovies : Response is Empty"))
            }
        }
    }

    override fun getNowPlayingMoviesPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.NOW_PLAYING_MOVIES,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getUpcomingMoviesPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.UPCOMING_MOVIES,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getPopularMoviesPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.POPULAR_MOVIES,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getTopRatedMoviesPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.TOP_RATED_MOVIES,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getOnWatchlistMovies(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getOnWatchlistMovies().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))
    }

    override fun getMovieDetail(movieId: Int): Flow<Status<Multi>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getMultiById(movieId).firstOrNull()
        if (localData != null) emit(Status.Success( DataMapper.mapMultiEntityToDomain(localData) ))

        when ( val apiResponse = remoteDataSource.getMovieDetail(movieId).first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = DataMapper.mapMovieDetailResponseToEntity(apiResponse.data)
                mappedToLocal.apply {
                    isTrending = localDataSource.isMultiTrending(this.id)
                    isNowPlaying = localDataSource.isMultiNowPlaying(this.id)
                    isUpcoming = localDataSource.isMultiUpcoming(this.id)
                    isPopular = localDataSource.isMultiPopular(this.id)
                    isTopRated = localDataSource.isMultiTopRated(this.id)
                    isOnWatchlist = localDataSource.isMultiOnWatchlist(this.id)
                }
                localDataSource.upsertMulti(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = DataMapper.mapMultiEntityToDomain(mappedToLocal)
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getMovieDetail : Response is Empty"))
            }
        }
    }

    override fun getMovieTrailers(movieId: Int): Flow<Status<List<Trailer>>> = flow {
        emit(Status.Loading())

        when ( val apiResponse = remoteDataSource.getMovieTrailer(movieId).first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val trailerVideos = apiResponse.data.results.filter {
                    it.type == Constants.VIDEO_TYPE_TRAILER || it.type == Constants.VIDEO_TYPE_TEASER
                }
                val mappedToLocal = trailerVideos.map { DataMapper.mapTrailerResponseToEntity(it) }
                mappedToLocal.map { it.multiId = movieId }
                localDataSource.upsertTrailers(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapTrailerEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))

                // Load & Emit Data from Local
                val localData = localDataSource.getTrailersById(movieId).firstOrNull()
                if (!localData.isNullOrEmpty()) emit(Status.Success(localData.map { DataMapper.mapTrailerEntityToDomain(it) }))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getMovieTrailers : Response is Empty"))
            }
        }
    }
    /** endregion Movies **/

    /** region TV Shows **/
    override fun getPopularTVShows(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getPopularTVShows().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getPopularTVShows().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.mediaType = Constants.MEDIA_TYPE_TV_SHOW
                    it.isPopular = true

                    it.isTrending = localDataSource.isMultiTrending(it.id)
                    it.isUpcoming = localDataSource.isMultiUpcoming(it.id)
                    it.isNowPlaying = localDataSource.isMultiNowPlaying(it.id)
                    it.isTopRated = localDataSource.isMultiTopRated(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getPopularTVShows : Response is Empty"))
            }
        }
    }

    override fun getTopRatedTVShows(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getTopRatedTVShows().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))

        when ( val apiResponse = remoteDataSource.getTopRatedTVShows().first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = apiResponse.data.results.map { DataMapper.mapMultiResponseToEntity(it) }
                mappedToLocal.map {
                    it.mediaType = Constants.MEDIA_TYPE_TV_SHOW
                    it.isTopRated = true

                    it.isTrending = localDataSource.isMultiTrending(it.id)
                    it.isUpcoming = localDataSource.isMultiUpcoming(it.id)
                    it.isNowPlaying = localDataSource.isMultiNowPlaying(it.id)
                    it.isPopular = localDataSource.isMultiPopular(it.id)
                    it.isOnWatchlist = localDataSource.isMultiOnWatchlist(it.id)
                }
                localDataSource.upsertMulties(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapMultiEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getTopRatedTVShows : Response is Empty"))
            }
        }
    }

    override fun getPopularTVShowsPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.POPULAR_TV_SHOWS,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getTopRatedTVShowsPaging(): Flow<PagingData<MultiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MultiPagingSource(
                    Constants.TOP_RATED_TV_SHOWS,
                    remoteDataSource,
                )
            }
        ).flow
    }

    override fun getOnWatchlistTVShows(): Flow<Status<List<Multi>>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getOnWatchlistTVShows().firstOrNull()
        if (localData != null) emit(Status.Success(localData.map { DataMapper.mapMultiEntityToDomain(it) }))
    }

    override fun getTVShowDetail(tvId: Int): Flow<Status<Multi>> = flow {
        emit(Status.Loading())

        // Load & Emit Data from Local
        val localData = localDataSource.getMultiById(tvId).firstOrNull()
        if (localData != null) emit(Status.Success( DataMapper.mapMultiEntityToDomain(localData) ))

        when ( val apiResponse = remoteDataSource.getTVShowDetail(tvId).first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val mappedToLocal = DataMapper.mapTVShowDetailResponseToEntity(apiResponse.data)
                mappedToLocal.apply {
                    isTrending = localDataSource.isMultiTrending(this.id)
                    isPopular = localDataSource.isMultiPopular(this.id)
                    isTopRated = localDataSource.isMultiTopRated(this.id)
                    isOnWatchlist = localDataSource.isMultiOnWatchlist(this.id)
                }
                localDataSource.upsertMulti(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = DataMapper.mapMultiEntityToDomain(mappedToLocal)
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getTVShowDetail : Response is Empty"))
            }
        }
    }

    override fun getTVShowTrailers(tvId: Int): Flow<Status<List<Trailer>>> = flow {
        emit(Status.Loading())

        when ( val apiResponse = remoteDataSource.getTVShowTrailer(tvId).first() ) {
            is ApiResponse.Success -> {
                // Insert data to local after successfully load data from network
                val trailerVideos = apiResponse.data.results.filter {
                    it.type == Constants.VIDEO_TYPE_TRAILER || it.type == Constants.VIDEO_TYPE_TEASER
                }
                val mappedToLocal = trailerVideos.map { DataMapper.mapTrailerResponseToEntity(it) }
                mappedToLocal.map { it.multiId = tvId }
                localDataSource.upsertTrailers(mappedToLocal)

                // Map local data to domain and emit
                val mappedToDomain = mappedToLocal.map { DataMapper.mapTrailerEntityToDomain(it) }
                emit(Status.Success(mappedToDomain))
            }
            is ApiResponse.Error -> {
                emit(Status.Error(apiResponse.errorMessage))

                // Load & Emit Data from Local
                val localData = localDataSource.getTrailersById(tvId).firstOrNull()
                if (!localData.isNullOrEmpty()) emit(Status.Success(localData.map { DataMapper.mapTrailerEntityToDomain(it) }))
            }
            is ApiResponse.Empty -> {
                emit(Status.Error("Unexpected Error getTVShowTrailers : Response is Empty"))
            }
        }
    }
    /** endregion TV Shows **/
}