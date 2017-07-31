package br.com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Movie implements Parcelable {

    private Long id;
    private String posterUrl;
    private String name;
    private String originalName;
    private Float voteAverage;
    private Float popularity;
    private String overview;
    private Calendar releaseDate;

    public Movie(){}

    protected Movie(Parcel in) {
        id = in.readLong();
        posterUrl = in.readString();
        name = in.readString();
        originalName = in.readString();
        voteAverage = in.readFloat();
        popularity = in.readFloat();
        overview = in.readString();
        releaseDate = (Calendar) in.readSerializable();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Float getVoteAverage() {
        if (voteAverage == null)
            voteAverage = 0f;
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isEmpty() {
        return id == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(id);
        dest.writeString(posterUrl);
        dest.writeString(name);
        dest.writeString(originalName);
        dest.writeFloat(voteAverage);
        dest.writeFloat(popularity);
        dest.writeString(overview);
        dest.writeSerializable(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
