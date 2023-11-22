package com.isfandroid.whattowatch.core.data.source.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class IntListTypeConverter {

    @TypeConverter
    fun listToIntString(list: List<Int>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToIntList(jsonString: String?): List<Int> {
        if (jsonString.isNullOrEmpty()) {
            return emptyList()
        }

        return Gson().fromJson(jsonString, Array<Int>::class.java)?.toList() ?: emptyList()
    }
}