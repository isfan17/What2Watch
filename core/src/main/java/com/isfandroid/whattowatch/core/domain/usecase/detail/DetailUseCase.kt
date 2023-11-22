package com.isfandroid.whattowatch.core.domain.usecase.detail

import com.isfandroid.whattowatch.core.data.Status
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.model.Trailer
import kotlinx.coroutines.flow.Flow

interface DetailUseCase {
    fun getMovieDetail(movieId: Int): Flow<Status<Multi>>
    fun getMovieTrailers(movieId: Int): Flow<Status<List<Trailer>>>

    fun getTVShowDetail(tvId: Int): Flow<Status<Multi>>
    fun getTVShowTrailers(tvId: Int): Flow<Status<List<Trailer>>>

    fun setMultiToWatchlist(multiId: Int, mediaType: String): Flow<String>
    fun removeMultiFromWatchlist(multiId: Int, mediaType: String): Flow<String>
}