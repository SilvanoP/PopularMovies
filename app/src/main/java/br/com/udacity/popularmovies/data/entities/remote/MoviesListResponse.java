package br.com.udacity.popularmovies.data.entities.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;

public class MoviesListResponse {

    @SerializedName("results")
    private List<Movie> movies;

    public MoviesListResponse() {
        movies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
