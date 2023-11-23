package com.isfandroid.whattowatch.core.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.isfandroid.whattowatch.core.data.source.remote.RemoteDataSource
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.utils.Constants
import com.isfandroid.whattowatch.core.utils.DataMapper

class MultiPagingSource(
    private val multiType: String,
    private val remoteDataSource: RemoteDataSource,
    private val query: String? = null,
): PagingSource<Int, Multi>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Multi> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val responseData = when (multiType) {
                Constants.TRENDING_MULTI -> {
                    remoteDataSource.getTrendingThisWeekPaging(page = position).results
                }
                Constants.NOW_PLAYING_MOVIES -> {
                    remoteDataSource.getNowPlayingMoviesPaging(page = position).results
                }
                Constants.UPCOMING_MOVIES -> {
                    remoteDataSource.getUpcomingMoviesPaging(page = position).results
                }
                Constants.POPULAR_MOVIES -> {
                    remoteDataSource.getPopularMoviesPaging(page = position).results
                }
                Constants.TOP_RATED_MOVIES -> {
                    remoteDataSource.getTopRatedMoviesPaging(page = position).results
                }
                Constants.POPULAR_TV_SHOWS -> {
                    remoteDataSource.getPopularTVShowsPaging(page = position).results
                }
                Constants.TOP_RATED_TV_SHOWS -> {
                    remoteDataSource.getTopRatedTVShowsPaging(page = position).results
                }
                // SEARCH MULTI
                else -> {
                    remoteDataSource.searchMulti(
                        page = position,
                        query = query!!,
                    ).results.filter { it.mediaType != Constants.MEDIA_TYPE_PERSON }
                }
            }
            val mappedResponse = responseData.map { DataMapper.mapMultiResponseToDomain(it) }

            LoadResult.Page(
                data = mappedResponse,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (mappedResponse.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Multi>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}