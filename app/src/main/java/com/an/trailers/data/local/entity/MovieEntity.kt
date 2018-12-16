package com.an.trailers.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import com.an.trailers.AppConstants
import com.an.trailers.data.local.converter.*
import com.an.trailers.data.remote.model.Cast
import com.an.trailers.data.remote.model.Crew
import com.an.trailers.data.remote.model.Genre
import com.an.trailers.data.remote.model.Video
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

@Entity(primaryKeys = ["id"])
data class MovieEntity(
        @SerializedName("id")
        val id: Long,

        var page: Long,
        var totalPages: Long,

        @SerializedName(value = "header", alternate = ["title", "name"])
        val header: String,

        @SerializedName("poster_path")
        var posterPath: String?,

        @SerializedName(value = "description", alternate = ["overview", "synopsis"])
        var description: String?,

        @SerializedName("release_date")
        var releaseDate: String?,

        @TypeConverters(GenreListTypeConverter::class)
        var genres: List<Genre>? = ArrayList(),

        @SerializedName("videos")
        @TypeConverters(VideoListTypeConverter::class)
        var videos: List<Video>? = ArrayList(),


        @TypeConverters(StringListConverter::class)
        var categoryTypes: List<String>? = ArrayList(),

        @TypeConverters(CrewListTypeConverter::class)
        var crews: List<Crew>? = ArrayList(),

        @TypeConverters(CastListTypeConverter::class)
        var casts: List<Cast>? = ArrayList(),

        @TypeConverters(MovieListTypeConverter::class)
        var similarMovies: List<MovieEntity>? = ArrayList(),

        @SerializedName("runtime")
        var runTime: Long,
        var status: String?
) : Parcelable {
    fun getFormattedPosterPath(): String? {
        if (posterPath != null && !posterPath!!.startsWith("http")) {
            posterPath = String.format(AppConstants.IMAGE_URL, posterPath)
        }
        return posterPath
    }

    fun isLastPage() : Boolean {
        return page >= totalPages
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readLong(),
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(Genre.CREATOR),
            source.createTypedArrayList(Video.CREATOR),
            source.createStringArrayList(),
            source.createTypedArrayList(Crew.CREATOR),
            source.createTypedArrayList(Cast.CREATOR),
            source.createTypedArrayList(MovieEntity.CREATOR),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeLong(page)
        writeLong(totalPages)
        writeString(header)
        writeString(posterPath)
        writeString(description)
        writeString(releaseDate)
        writeTypedList(genres)
        writeTypedList(videos)
        writeStringList(categoryTypes)
        writeTypedList(crews)
        writeTypedList(casts)
        writeTypedList(similarMovies)
        writeLong(runTime)
        writeString(status)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieEntity> = object : Parcelable.Creator<MovieEntity> {
            override fun createFromParcel(source: Parcel): MovieEntity = MovieEntity(source)
            override fun newArray(size: Int): Array<MovieEntity?> = arrayOfNulls(size)
        }
    }
}
