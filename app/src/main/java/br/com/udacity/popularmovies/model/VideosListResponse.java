package br.com.udacity.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silvano Damasceno on 15/08/2017.
 */

public class VideosListResponse {

    private Long id; // this refers to movie id
    @SerializedName("results")
    private List<Video> videos;

    public VideosListResponse() {
        videos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
