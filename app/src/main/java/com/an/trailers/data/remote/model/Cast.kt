package com.an.trailers.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Cast(
    val id: Long,
    @SerializedName("cast_id")
    val castId: Long,
    var character: String?,
    @SerializedName("credit_id")
    val creditId: String,
    val name: String?,
    @SerializedName("profile_path")
    var profilePath: String?,
    val order: Int
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeLong(castId)
        writeString(character)
        writeString(creditId)
        writeString(name)
        writeString(profilePath)
        writeInt(order)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cast> = object : Parcelable.Creator<Cast> {
            override fun createFromParcel(source: Parcel): Cast = Cast(source)
            override fun newArray(size: Int): Array<Cast?> = arrayOfNulls(size)
        }
    }
}
