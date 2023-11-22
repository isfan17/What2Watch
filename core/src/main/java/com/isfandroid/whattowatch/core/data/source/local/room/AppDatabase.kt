package com.isfandroid.whattowatch.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.isfandroid.whattowatch.core.data.source.local.converters.IntListTypeConverter
import com.isfandroid.whattowatch.core.data.source.local.converters.StringListTypeConverter
import com.isfandroid.whattowatch.core.data.source.local.entity.MultiEntity
import com.isfandroid.whattowatch.core.data.source.local.entity.TrailerEntity

@Database(
    entities = [
        MultiEntity::class,
        TrailerEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(
    StringListTypeConverter::class,
    IntListTypeConverter::class,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao
}