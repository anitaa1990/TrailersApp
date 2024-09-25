package com.an.trailers.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class VideoResponse implements Parcelable {

    private long id;
    private List<Video> results;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeList(this.results);
    }

    public VideoResponse() {
    }

    protected VideoResponse(Parcel in) {
        this.id = in.readLong();
        this.results = new ArrayList<Video>();
        in.readList(this.results, Video.class.getClassLoader());
    }

    public static final Creator<VideoResponse> CREATOR = new Creator<VideoResponse>() {
        @Override
        public VideoResponse createFromParcel(Parcel source) {
            return new VideoResponse(source);
        }

        @Override
        public VideoResponse[] newArray(int size) {
            return new VideoResponse[size];
        }
    };
}
