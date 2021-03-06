package br.com.udacity.popularmovies.feature.shared;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.data.entities.Review;
import br.com.udacity.popularmovies.data.entities.Video;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface MoviesRepository {

    Single<List<Movie>> getMoviesFromCategory();
    Single<List<Movie>> loadNextPage(int position);
    MovieCategory getCategory();
    void changeCategory(MovieCategory category);
    Maybe<List<Movie>> searchMovieByTitle(String title);
    void setSelectedMovie(Movie movie);
    Movie getSelectedMovie();
    Maybe<Movie> isFavorite();
    Single<List<Video>> getTrailers();
    Single<List<Review>> getReviews();
    Single<Boolean> changeFavoriteState();
    String getTrailerUrl(int position);
}
