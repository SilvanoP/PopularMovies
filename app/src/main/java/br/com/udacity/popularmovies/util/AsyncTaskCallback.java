package br.com.udacity.popularmovies.util;

import java.util.List;

import br.com.udacity.popularmovies.model.Movie;

/**
 * Created by Silvano Damasceno on 05/08/2017.
 */

public interface AsyncTaskCallback {

    void onAsyncTaskComplete(List<Movie> movies);
}
