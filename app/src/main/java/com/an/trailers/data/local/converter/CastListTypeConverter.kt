package com.an.trailers.data.local.converter

import android.arch.persistence.room.TypeConverter
import com.an.trailers.data.remote.model.Cast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CastListTypeConverter {

    @TypeConverter
    fun fromString(value: String): List<Cast>? {
        val listType = object : TypeToken<List<Cast>>() {}.type
        return Gson().fromJson<List<Cast>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Cast>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
