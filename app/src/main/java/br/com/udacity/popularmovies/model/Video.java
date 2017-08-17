package br.com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    private String id;
    private String key;
    private String name;

    public Video() {}

    public Video(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int i) {
            return new Video[i];
        }
    };
}
