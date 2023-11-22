package com.isfandroid.whattowatch.core.data.source.remote.response.movie

import com.google.gson.annotations.SerializedName
import com.isfandroid.whattowatch.core.data.source.remote.response.general.GenreResponse

data class MovieDetailResponse(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("release_date")
    val releaseDate: String,

    @field:SerializedName("overview")
    val overview: String?,

    @field:SerializedName("tagline")
    val tagline: String?,

    @field:SerializedName("genres")
    val genres: List<GenreResponse>,

    @field:SerializedName("runtime")
    val runtime: Int?,

    @field:SerializedName("vote_average")
    val voteAverage: Double,

    @field:SerializedName("popularity")
    val popularity: Double,

    @field:SerializedName("poster_path")
    val posterPath: String?,

    @field:SerializedName("backdrop_path")
    val backdropPath: String?,
)
