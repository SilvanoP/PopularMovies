package br.com.udacity.popularmovies.data.repository;

import android.content.SharedPreferences;

import java.util.List;

import javax.inject.Inject;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.data.webservice.TheMovieDBClient;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;
import br.com.udacity.popularmovies.util.Constants;

public class MoviesRepositoryImpl implements MoviesRepository {

    private SharedPreferences preferences;
    private TheMovieDBClient client;

    @Inject
    public MoviesRepositoryImpl(SharedPreferences preferences, TheMovieDBClient client) {
        this.preferences = preferences;
        this.client = client;
    }

    @Override
    public int getMovieCategory() {
        return preferences.getInt(Constants.PREFERENCE_SORT_MOVIES, MovieCategory.POPULAR.getValue());
    }

    @Override
    public List<Movie> getMoviesFromCategory(MovieCategory category) {
        return null;
    }

    @Override
    public void setSelectedMovie(Movie movie) {

    }
}
