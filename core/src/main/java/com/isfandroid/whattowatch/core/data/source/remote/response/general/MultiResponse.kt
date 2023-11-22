package com.isfandroid.whattowatch.core.data.source.remote.response.general

import com.google.gson.annotations.SerializedName

data class MultiResponse (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("media_type")
    val mediaType: String?,

    @field:SerializedName("backdrop_path")
    val backdropPath: String?,

    @field:SerializedName("poster_path")
    val posterPath: String?,

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("release_date")
    val releaseDate: String?,

    @field:SerializedName("first_air_date")
    val firstAirDate: String?,

    @field:SerializedName("vote_average")
    val voteAverage: Double,
)