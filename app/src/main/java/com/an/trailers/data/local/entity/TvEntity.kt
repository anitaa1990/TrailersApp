package com.an.trailers.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import com.an.trailers.AppConstants
import com.an.trailers.data.local.converter.CastListTypeConverter
import com.an.trailers.data.local.converter.CrewListTypeConverter
import com.an.trailers.data.local.converter.StringListConverter
import com.an.trailers.data.local.converter.TvListTypeConverter
import com.an.trailers.data.remote.model.Cast
import com.an.trailers.data.remote.model.Crew
import com.an.trailers.data.remote.model.Genre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

@Entity(primaryKeys = ["id"])
data class TvEntity(
    @SerializedName("id")
    val id: Long,

    @SerializedName(value = "header", alternate = ["title", "name"])
    val header: String,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName(value = "description", alternate = ["overview", "synopsis"])
    var description: String?,

    @SerializedName("release_date")
    var releaseDate: String?,

    @SerializedName("genres")
    var genres: List<Genre>? = ArrayList(),

    @SerializedName("videos")
    @TypeConverters(StringListConverter::class)
    var videos: List<String>? = ArrayList(),

    @TypeConverters(CrewListTypeConverter::class)
    var crews: List<Crew>? = ArrayList(),

    @TypeConverters(CastListTypeConverter::class)
    var casts: List<Cast>? = ArrayList(),

    @TypeConverters(TvListTypeConverter::class)
    var similarTvEntities: List<TvEntity>? = ArrayList(),

    @SerializedName("number_of_seasons")
    var numberOfSeasons: Long?,
    var status: String?,
    var categoryType: String?
) : Parcelable {
    fun getFormattedPosterPath(): String? {
        if (posterPath != null && !posterPath!!.startsWith("http")) {
            posterPath = String.format(AppConstants.IMAGE_URL, posterPath)
        }
        return posterPath
    }

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.createTypedArrayList(Genre.CREATOR),
        source.createStringArrayList(),
        source.createTypedArrayList(Crew.CREATOR),
        source.createTypedArrayList(Cast.CREATOR),
        source.createTypedArrayList(TvEntity.CREATOR),
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(header)
        writeString(posterPath)
        writeString(description)
        writeString(releaseDate)
        writeTypedList(genres)
        writeStringList(videos)
        writeTypedList(crews)
        writeTypedList(casts)
        writeTypedList(similarTvEntities)
        writeValue(numberOfSeasons)
        writeString(status)
        writeString(categoryType)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TvEntity> = object : Parcelable.Creator<TvEntity> {
            override fun createFromParcel(source: Parcel): TvEntity = TvEntity(source)
            override fun newArray(size: Int): Array<TvEntity?> = arrayOfNulls(size)
        }
    }
}
