package com.isfandroid.whattowatch.core.data.source.remote.response.tvshow

import com.google.gson.annotations.SerializedName
import com.isfandroid.whattowatch.core.data.source.remote.response.general.GenreResponse

data class TVShowDetailResponse (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("first_air_date")
    val firstAirDate: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("tagline")
    val tagline: String?,

    @field:SerializedName("genres")
    val genres: List<GenreResponse>,

    @field:SerializedName("episode_run_time")
    val episodeRunTime: List<Int>,

    @field:SerializedName("vote_average")
    val voteAverage: Double,

    @field:SerializedName("popularity")
    val popularity: Double,

    @field:SerializedName("poster_path")
    val posterPath: String?,

    @field:SerializedName("backdrop_path")
    val backdropPath: String?,
)