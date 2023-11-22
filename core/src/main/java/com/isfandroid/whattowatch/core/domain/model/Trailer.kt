package com.isfandroid.whattowatch.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailer(
    val id: String,
    val site: String,
    val name: String,
    val official: Boolean,
    val type: String,
    val key: String,
): Parcelable
