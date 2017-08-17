package br.com.udacity.popularmovies.util.tasks;

import java.util.List;

import br.com.udacity.popularmovies.model.Movie;

public interface AsyncTaskCallback {

    void onAsyncTaskComplete(List<Movie> movies);
}
