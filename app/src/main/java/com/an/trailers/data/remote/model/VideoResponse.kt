package com.an.trailers.data.remote.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

class VideoResponse : Parcelable {

    var id: Long = 0
    var results: List<Video>? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeList(this.results)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.id = `in`.readLong()
        this.results = ArrayList()
        `in`.readList(this.results, Video::class.java.classLoader)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<VideoResponse> = object : Parcelable.Creator<VideoResponse> {
            override fun createFromParcel(source: Parcel): VideoResponse {
                return VideoResponse(source)
            }

            override fun newArray(size: Int): Array<VideoResponse?> {
                return arrayOfNulls(size)
            }
        }
    }
}
