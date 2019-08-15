package br.com.udacity.popularmovies.feature.shared;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import io.reactivex.Single;

public interface MoviesRepository {

    Single<List<Movie>> getMoviesFromCategory();
    Single<List<Movie>> loadNextPage(int position);
    MovieCategory getCategory();
    void changeCategory(MovieCategory category);
    void setSelectedMovie(Movie movie);
}
