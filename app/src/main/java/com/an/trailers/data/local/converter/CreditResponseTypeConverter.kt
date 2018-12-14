package com.an.trailers.data.local.converter

import android.arch.persistence.room.TypeConverter
import com.an.trailers.data.remote.model.CreditResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreditResponseTypeConverter {

    @TypeConverter
    fun fromString(value: String): CreditResponse? {
        val listType = object : TypeToken<CreditResponse>() {}.type
        return Gson().fromJson<CreditResponse>(value, listType)
    }

    @TypeConverter
    fun fromList(list: CreditResponse?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
