package br.com.udacity.popularmovies.feature.listmovies;

import javax.inject.Inject;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.feature.shared.BasePresenterImpl;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;

public class ListMoviesPresenter extends BasePresenterImpl<ListMoviesContract.View>
        implements ListMoviesContract.Presenter {

    private MoviesRepository repository;

    @Inject
    ListMoviesPresenter(MoviesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void changeCategory(String category) {

    }

    @Override
    public void onMovieSelected(Movie movie) {

    }
}
