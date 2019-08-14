package br.com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Movie implements Parcelable {

    private Long id;
    @SerializedName("poster_path")
    private String posterUrl;
    @SerializedName("backdrop_path")
    private String backdropUrl;
    @SerializedName("title")
    private String name;
    @SerializedName("original_title")
    private String originalName;
    @SerializedName("vote_average")
    private Float voteAverage;
    private String overview;
    @SerializedName("release_date")
    private Date releaseDate;
    private transient boolean isFavorite;

    public Movie(){
        isFavorite = false;
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        posterUrl = in.readString();
        backdropUrl = in.readString();
        name = in.readString();
        originalName = in.readString();
        voteAverage = in.readFloat();
        overview = in.readString();
        releaseDate = new Date(in.readLong());
        isFavorite = in.readInt() != 0;
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

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
        dest.writeString(backdropUrl);
        dest.writeString(name);
        dest.writeString(originalName);
        dest.writeFloat(voteAverage);
        dest.writeString(overview);
        dest.writeLong(releaseDate.getTime());
        int favorite = isFavorite ? 1 : 0;
        dest.writeInt(favorite);
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
