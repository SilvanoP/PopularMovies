package br.com.udacity.popularmovies.feature.listmovies;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.feature.shared.BasePresenter;
import br.com.udacity.popularmovies.feature.shared.BaseView;

public interface ListMoviesContract {

    interface View extends BaseView {
        void showProgress();
        void hideProgress();
        void noMoviesFound();
        void refreshMovieList(List<Movie> movies);
        void addMovies(List<Movie> movies);
        void showSortDialog(MovieCategory category);
        void goToMovieDetail();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData();
        void onScrollList(int position);
        void onSortMenuSelected();
        void changeCategory(MovieCategory category);
        void onMovieSelected(Movie movie);
        void searchMovie(String movieTitle);
    }
}
