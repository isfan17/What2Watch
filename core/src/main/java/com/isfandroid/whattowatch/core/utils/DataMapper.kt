package com.isfandroid.whattowatch.core.utils

import com.isfandroid.whattowatch.core.data.source.local.entity.MultiEntity
import com.isfandroid.whattowatch.core.data.source.local.entity.TrailerEntity
import com.isfandroid.whattowatch.core.data.source.remote.response.general.GenreResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.general.MultiResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.general.TrailerResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.movie.MovieDetailResponse
import com.isfandroid.whattowatch.core.data.source.remote.response.tvshow.TVShowDetailResponse
import com.isfandroid.whattowatch.core.domain.model.Multi
import com.isfandroid.whattowatch.core.domain.model.Trailer

object DataMapper {

    fun mapMultiEntityToDomain(input: MultiEntity) = Multi(
        id = input.id,
        mediaType = input.mediaType.orEmpty(),
        backdropPath = input.backdropPath,
        posterPath = input.posterPath,
        title = input.title,
        name = input.name,
        overview = input.overview,
        releaseDate = input.releaseDate,
        firstAirDate = input.firstAirDate,
        voteAverage = input.voteAverage,
        tagline = input.tagline,
        genres = input.genres,
        runtime = input.runtime,
        episodeRunTime = input.episodeRunTime,
        isTrending = input.isTrending,
        isNowPlaying = input.isNowPlaying,
        isUpcoming = input.isUpcoming,
        isPopular = input.isPopular,
        isTopRated = input.isTopRated,
        isOnWatchlist = input.isOnWatchlist,
    )

    fun mapMultiResponseToEntity(input: MultiResponse) = MultiEntity(
        id = input.id,
        mediaType = input.mediaType,
        backdropPath = input.backdropPath,
        posterPath = input.posterPath,
        title = input.title,
        name = input.name,
        overview = input.overview,
        releaseDate = input.releaseDate,
        firstAirDate = input.firstAirDate,
        voteAverage = input.voteAverage,
        tagline = null,
        genres = null,
        runtime = null,
        episodeRunTime = null,
    )

    fun mapMovieDetailResponseToEntity(input: MovieDetailResponse) = MultiEntity(
        id = input.id,
        mediaType = Constants.MEDIA_TYPE_MOVIE,
        backdropPath = input.backdropPath,
        posterPath = input.posterPath,
        title = input.title,
        overview = input.overview,
        releaseDate = input.releaseDate,
        voteAverage = input.voteAverage,
        tagline = input.tagline,
        genres = mapGenreResponsesToEntities(input.genres),
        runtime = input.runtime,
        // TV Show Attribute
        name = null,
        firstAirDate = null,
        episodeRunTime = null,
    )

    fun mapTVShowDetailResponseToEntity(input: TVShowDetailResponse) = MultiEntity(
        id = input.id,
        mediaType = Constants.MEDIA_TYPE_TV_SHOW,
        backdropPath = input.backdropPath,
        posterPath = input.posterPath,
        name = input.name,
        firstAirDate = input.firstAirDate,
        episodeRunTime = input.episodeRunTime,
        overview = input.overview,
        voteAverage = input.voteAverage,
        tagline = input.tagline,
        genres = mapGenreResponsesToEntities(input.genres),
        // Movie Attribute
        title = null,
        releaseDate = null,
        runtime = null,
    )

    private fun mapGenreResponsesToEntities(input: List<GenreResponse>): List<String> {
        val genreValues = mutableListOf<String>()
        input.map { genreValues.add(it.name) }
        return genreValues
    }

    fun mapTrailerEntityToDomain(input: TrailerEntity) = Trailer (
        id = input.id,
        site = input.site,
        name = input.name,
        official = input.official,
        type = input.type,
        key = input.key,
    )

    fun mapTrailerResponseToEntity(input: TrailerResponse) = TrailerEntity (
        id = input.id,
        site = input.site,
        name = input.name,
        official = input.official,
        type = input.type,
        key = input.key,
        multiId = null,
    )
}