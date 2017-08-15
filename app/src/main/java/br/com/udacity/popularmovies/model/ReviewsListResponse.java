package br.com.udacity.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silvano Damasceno on 14/08/2017.
 */

public class ReviewsListResponse {

    private Long id; // this refers to movie id
    private int page;
    private List<Review> reviews;

    public ReviewsListResponse() {
        page = 0;
        reviews = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
