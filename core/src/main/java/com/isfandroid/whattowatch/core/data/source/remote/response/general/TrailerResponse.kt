package com.isfandroid.whattowatch.core.data.source.remote.response.general

import com.google.gson.annotations.SerializedName

data class TrailerResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("site")
    val site: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("official")
    val official: Boolean,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("key")
    val key: String,
)
