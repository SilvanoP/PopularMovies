package br.com.udacity.popularmovies.util;

import br.com.udacity.popularmovies.model.MoviesListResponse;
import br.com.udacity.popularmovies.model.ReviewsListResponse;
import br.com.udacity.popularmovies.model.VideosListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBClient {

    @GET("/3/movie/popular")
    Call<MoviesListResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
    @GET("/3/movie/top_rated")
    Call<MoviesListResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewsListResponse> getReviews(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );
    @GET("/3/movie/{movie_id}/videos")
    Call<VideosListResponse> getVideos(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );
}
