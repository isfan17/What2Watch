package com.isfandroid.whattowatch.core.data.source.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class StringListTypeConverter {

    @TypeConverter
    fun listToString(list: List<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToList(jsonString: String?): List<String> {
        if (jsonString.isNullOrEmpty()) {
            return emptyList()
        }

        return Gson().fromJson(jsonString, Array<String>::class.java)?.toList() ?: emptyList()
    }
}