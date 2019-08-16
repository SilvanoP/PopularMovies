package br.com.udacity.popularmovies.feature.listmovies;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.feature.shared.BasePresenterImpl;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListMoviesPresenter extends BasePresenterImpl<ListMoviesContract.View>
        implements ListMoviesContract.Presenter {

    private MoviesRepository repository;

    @Inject
    ListMoviesPresenter(MoviesRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void loadData() {
        weakView.get().showProgress();
        disposable.add(repository.getMoviesFromCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) {
                        weakView.get().hideProgress();
                        weakView.get().refreshMovieList(movies);
                    }
                })
        );
    }

    @Override
    public void onScrollList(int position) {
        disposable.add(repository.loadNextPage(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) {
                        weakView.get().addMovies(movies);
                    }
                })
        );
    }

    @Override
    public void onSortMenuSelected() {
        weakView.get().showSortDialog(repository.getCategory());
    }

    @Override
    public void changeCategory(MovieCategory category) {
        weakView.get().showProgress();
        repository.changeCategory(category);
        disposable.add(repository.getMoviesFromCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) {
                        weakView.get().hideProgress();
                        weakView.get().refreshMovieList(movies);
                    }
                })
        );
    }

    @Override
    public void onMovieSelected(Movie movie) {
        repository.setSelectedMovie(movie);
        weakView.get().goToMovieDetail();
    }

    @Override
    public void searchMovie(String movieTitle) {
        weakView.get().showProgress();
        disposable.add(repository.searchMovieByTitle(movieTitle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) {
                        weakView.get().hideProgress();
                        if (movies.size() == 0) {
                            weakView.get().noMoviesFound();
                        } else {
                            weakView.get().refreshMovieList(movies);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        weakView.get().hideProgress();
                        weakView.get().noMoviesFound();
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        weakView.get().noMoviesFound();
                    }
                }));
    }
}
