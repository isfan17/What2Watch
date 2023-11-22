package com.isfandroid.whattowatch.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val backdropPath: String,
    val posterPath: String,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val voteAverage: Double,

    val tagline: String?,
    val genres: String?,
    val runtime: Int?,
): Parcelable
