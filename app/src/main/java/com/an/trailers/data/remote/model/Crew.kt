package com.an.trailers.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Crew(
    val id: Long,
    @SerializedName("credit_id")
    val creditId: String,
    var name: String?,
    @SerializedName("profile_path")
    var profilePath: String?,
    val job: String?,
    val department: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(creditId)
        writeString(name)
        writeString(profilePath)
        writeString(job)
        writeString(department)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Crew> = object : Parcelable.Creator<Crew> {
            override fun createFromParcel(source: Parcel): Crew = Crew(source)
            override fun newArray(size: Int): Array<Crew?> = arrayOfNulls(size)
        }
    }
}
