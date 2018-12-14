package com.an.trailers.data.local.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import com.an.trailers.AppConstants;
import com.an.trailers.data.local.converter.CastListTypeConverter;
import com.an.trailers.data.local.converter.CrewListTypeConverter;
import com.an.trailers.data.local.converter.MovieListTypeConverter;
import com.an.trailers.data.local.converter.StringListConverter;
import com.an.trailers.data.local.converter.VideoListTypeConverter;
import com.an.trailers.data.remote.model.Cast;
import com.an.trailers.data.remote.model.CreditResponse;
import com.an.trailers.data.remote.model.Crew;
import com.an.trailers.data.remote.model.Genre;
import com.an.trailers.data.remote.model.Video;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(primaryKeys = ("id"))
public class MovieEntity  implements Parcelable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName(value="header", alternate={"title", "name"})
    @Expose
    private String header;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName(value="description", alternate={"overview", "synopsis"})
    private String description;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;


    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;


    @SerializedName("videos")
    @Expose
    @TypeConverters(StringListConverter.class)
    private List<String> videos;

    @Expose
    @TypeConverters(CrewListTypeConverter.class)
    private List<Crew> crews = null;


    @Expose
    @TypeConverters(CastListTypeConverter.class)
    private List<Cast> casts = null;


    @Expose
    @TypeConverters(MovieListTypeConverter.class)
    private List<MovieEntity> similarMovies;

    @SerializedName("runtime")
    @Expose
    private Long runtime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("number_of_seasons")
    @Expose
    private Long numberOfSeasons;

    private String categoryType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPosterPath() {
        if(posterPath != null && !posterPath.startsWith("http")) {
            posterPath = String.format(AppConstants.IMAGE_URL, posterPath);
        }
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public List<Crew> getCrews() {
        return crews;
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

        public List<MovieEntity> getSimilarMovies() {
        return similarMovies;
    }

    public void setSimilarMovies(List<MovieEntity> similarMovies) {
        this.similarMovies = similarMovies;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Long numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.header);
        dest.writeString(this.posterPath);
        dest.writeString(this.description);
        dest.writeString(this.releaseDate);
        dest.writeTypedList(this.genres);
        dest.writeStringList(this.videos);
        dest.writeTypedList(this.crews);
        dest.writeTypedList(this.casts);
        dest.writeTypedList(this.similarMovies);
        dest.writeValue(this.runtime);
        dest.writeString(this.status);
        dest.writeValue(this.numberOfSeasons);
        dest.writeString(this.categoryType);
    }

    public MovieEntity() {
        this.casts = new ArrayList<>();
        this.crews = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.videos = new ArrayList<>();
        this.similarMovies = new ArrayList<>();
    }

    protected MovieEntity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.header = in.readString();
        this.posterPath = in.readString();
        this.description = in.readString();
        this.releaseDate = in.readString();
        this.genres = in.createTypedArrayList(Genre.CREATOR);
        this.videos = in.createStringArrayList();
        this.crews = in.createTypedArrayList(Crew.CREATOR);
        this.casts = in.createTypedArrayList(Cast.CREATOR);
        this.similarMovies = in.createTypedArrayList(MovieEntity.CREATOR);
        this.runtime = (Long) in.readValue(Long.class.getClassLoader());
        this.status = in.readString();
        this.numberOfSeasons = (Long) in.readValue(Long.class.getClassLoader());
        this.categoryType = in.readString();
    }

    public static final Creator<MovieEntity> CREATOR = new Creator<MovieEntity>() {
        @Override
        public MovieEntity createFromParcel(Parcel source) {
            return new MovieEntity(source);
        }

        @Override
        public MovieEntity[] newArray(int size) {
            return new MovieEntity[size];
        }
    };
}
