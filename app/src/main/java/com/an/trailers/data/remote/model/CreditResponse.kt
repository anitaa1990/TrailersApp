package com.an.trailers.data.remote.model

import androidx.room.TypeConverters
import com.an.trailers.data.local.converter.CastListTypeConverter
import com.an.trailers.data.local.converter.CrewListTypeConverter

data class CreditResponse(
    @TypeConverters(CrewListTypeConverter::class)
    var crew: List<Crew> = ArrayList(),
    @TypeConverters(CastListTypeConverter::class)
    var cast: List<Cast> = ArrayList()
)
