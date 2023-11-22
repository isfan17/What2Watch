package com.isfandroid.whattowatch.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trailers")
data class TrailerEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String,
    val site: String,
    val name: String,
    val official: Boolean,
    val type: String,
    val key: String,

    var multiId: Int?,
)