package com.isfandroid.whattowatch.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Multi(
    val id: Int,
    val mediaType: String?,
    val backdropPath: String?,
    val posterPath: String?,
    val title: String?,
    val name: String?,
    val overview: String?,
    val releaseDate: String?,
    val firstAirDate: String?,
    val voteAverage: Double,

    // Movie & TV Show Detail Attr
    var tagline: String?,
    var genres: List<String>?,

    // Movie Attr
    var runtime: Int?,

    // TV Show Attr
    var episodeRunTime: List<Int>?,

    // Status Filters
    var isTrending: Boolean = false,
    var isNowPlaying: Boolean = false,
    var isUpcoming: Boolean = false,
    var isPopular: Boolean = false,
    var isTopRated: Boolean = false,
    var isOnWatchlist: Boolean = false,
): Parcelable
