package com.an.trailers.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class CreditResponse implements Parcelable {

    private List<Cast> cast;
    private List<Crew> crew;

    public CreditResponse(List<Crew> crewList, List<Cast> casts) {
        this.cast = casts;
        this.crew = crewList;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.cast);
        dest.writeTypedList(this.crew);
    }

    public CreditResponse() {
    }

    protected CreditResponse(Parcel in) {
        this.cast = in.createTypedArrayList(Cast.CREATOR);
        this.crew = in.createTypedArrayList(Crew.CREATOR);
    }

    public static final Creator<CreditResponse> CREATOR = new Creator<CreditResponse>() {
        @Override
        public CreditResponse createFromParcel(Parcel source) {
            return new CreditResponse(source);
        }

        @Override
        public CreditResponse[] newArray(int size) {
            return new CreditResponse[size];
        }
    };
}
