package br.com.udacity.popularmovies.data.entities.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.data.entities.Review;

public class ReviewsListResponse {

    private Long id; // this refers to movie id
    @SerializedName("results")
    private List<Review> reviews;

    public ReviewsListResponse() {
        reviews = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
