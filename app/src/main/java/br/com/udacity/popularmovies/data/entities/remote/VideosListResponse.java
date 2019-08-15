package br.com.udacity.popularmovies.data.entities.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.data.entities.Video;

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
