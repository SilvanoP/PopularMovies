package br.com.udacity.popularmovies.feature.listmovies;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.feature.shared.BasePresenter;
import br.com.udacity.popularmovies.feature.shared.BaseView;

public interface ListMoviesContract {

    interface View extends BaseView {
        void refreshMovieList(List<Movie> movies);
        void goToMovieDetail();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData();
        void changeCategory(String category);
        void onMovieSelected(Movie movie);
    }
}
