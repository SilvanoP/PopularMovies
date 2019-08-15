package br.com.udacity.popularmovies.data.webservice;

import br.com.udacity.popularmovies.data.entities.remote.MoviesListResponse;
import br.com.udacity.popularmovies.data.entities.remote.ReviewsListResponse;
import br.com.udacity.popularmovies.data.entities.remote.VideosListResponse;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBClient {

    @GET("/3/movie/popular")
    Single<MoviesListResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
    @GET("/3/movie/top_rated")
    Single<MoviesListResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
    @GET("/3/movie/upcoming")
    Single<MoviesListResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );
    @GET("/3/movie/{movie_id}/reviews")
    Single<ReviewsListResponse> getReviews(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );
    @GET("/3/movie/{movie_id}/videos")
    Single<VideosListResponse> getVideos(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );
}
