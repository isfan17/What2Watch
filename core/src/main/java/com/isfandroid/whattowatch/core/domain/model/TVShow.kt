package com.isfandroid.whattowatch.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVShow(
    val id: Int,
    val originalName: String,
    val name: String,
    val overview: String,
    val firstAirDate: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val popularity: Double,

    val tagline: String?,
    val genres: String?,
    val episodeRunTime: List<Int>?,
): Parcelable
