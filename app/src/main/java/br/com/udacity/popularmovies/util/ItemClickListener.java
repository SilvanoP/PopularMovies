package br.com.udacity.popularmovies.util;

import br.com.udacity.popularmovies.data.entities.Movie;

/**
 * Interface used for onItemClick on adapter
 */
public interface ItemClickListener {
    public void onItemClick(Movie movie);
}
