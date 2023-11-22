package com.isfandroid.whattowatch.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.isfandroid.whattowatch.core.data.source.remote.response.general.GenreResponse

@Entity(tableName = "multi")
data class MultiEntity (

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    var mediaType: String?,
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

    // Saved Movie or TV Show
    var isOnWatchlist: Boolean = false,
)