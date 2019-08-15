package br.com.udacity.popularmovies.feature.shared;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;

public interface MoviesRepository {

    int getMovieCategory();
    List<Movie> getMoviesFromCategory(MovieCategory category);
    void setSelectedMovie(Movie movie);
}
