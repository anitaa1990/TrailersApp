package com.an.trailers.data.local.converter

import android.arch.persistence.room.TypeConverter
import com.an.trailers.data.remote.model.Crew
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CrewListTypeConverter {

    @TypeConverter
    fun fromString(value: String): List<Crew>? {
        val listType = object : TypeToken<List<Crew>>() {}.type
        return Gson().fromJson<List<Crew>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Crew>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
