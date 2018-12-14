package com.an.trailers.data.remote.model

import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import com.an.trailers.data.local.converter.CastListTypeConverter
import com.an.trailers.data.local.converter.CrewListTypeConverter
import java.util.ArrayList

data class CreditResponse(
    @TypeConverters(CrewListTypeConverter::class)
    var crew: List<Crew> = ArrayList(),
    @TypeConverters(CastListTypeConverter::class)
    var cast: List<Cast> = ArrayList()
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createTypedArrayList(Crew.CREATOR),
        source.createTypedArrayList(Cast.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(crew)
        writeTypedList(cast)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CreditResponse> = object : Parcelable.Creator<CreditResponse> {
            override fun createFromParcel(source: Parcel): CreditResponse = CreditResponse(source)
            override fun newArray(size: Int): Array<CreditResponse?> = arrayOfNulls(size)
        }
    }
}
