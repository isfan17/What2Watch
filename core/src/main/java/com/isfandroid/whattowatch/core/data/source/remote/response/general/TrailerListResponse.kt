package com.isfandroid.whattowatch.core.data.source.remote.response.general

import com.google.gson.annotations.SerializedName

data class TrailerListResponse(

    @field:SerializedName("results")
    val results: List<TrailerResponse>
)
