package com.isfandroid.whattowatch.core.data.source.remote.response.general

import com.google.gson.annotations.SerializedName

data class GenreResponse (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,
)