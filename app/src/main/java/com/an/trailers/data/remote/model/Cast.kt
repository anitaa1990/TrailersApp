package com.an.trailers.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable {

    @SerializedName("cast_id")
    private long castId;

    private String character;

    @SerializedName("credit_id")
    private String creditId;

    private long id;
    private String name;
    private int order;

    @SerializedName("profile_path")
    private String profilePath;

    public long getCastId() {
        return castId;
    }

    public void setCastId(long castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.castId);
        dest.writeString(this.character);
        dest.writeString(this.creditId);
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.order);
        dest.writeString(this.profilePath);
    }

    public Cast() {
    }

    protected Cast(Parcel in) {
        this.castId = in.readLong();
        this.character = in.readString();
        this.creditId = in.readString();
        this.id = in.readLong();
        this.name = in.readString();
        this.order = in.readInt();
        this.profilePath = in.readString();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
}
