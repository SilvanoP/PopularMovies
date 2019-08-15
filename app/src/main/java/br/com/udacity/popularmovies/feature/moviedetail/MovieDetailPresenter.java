package br.com.udacity.popularmovies.feature.moviedetail;

import java.util.List;

import javax.inject.Inject;

import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.Review;
import br.com.udacity.popularmovies.data.entities.Video;
import br.com.udacity.popularmovies.feature.shared.BasePresenterImpl;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;
import br.com.udacity.popularmovies.util.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailPresenter extends BasePresenterImpl<MovieDetailContract.View> implements MovieDetailContract.Presenter {

    private MoviesRepository repository;

    @Inject
    public MovieDetailPresenter(MoviesRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void loadData() {
        Movie movie = repository.getSelectedMovie();
        weakView.get().showBackdropImage(movie.getBackdropUrl());
        weakView.get().showPosterImage(movie.getPosterUrl());
        weakView.get().showTitle(movie.getName());
        weakView.get().showOriginalName(movie.getOriginalName());
        weakView.get().showReleaseDate(Utils.dateToString(movie.getReleaseDate(),"MM/dd/yyyy"));
        weakView.get().showRating(movie.getVoteAverage());
        weakView.get().showOverview(movie.getOverview());
        disposable.add(repository.isFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) {
                        weakView.get().showFloatingButton(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        weakView.get().showFloatingButton(false);
                    }
                }));
        disposable.add(repository.getTrailers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) {
                        if (videos != null && videos.size() > 0) {
                            weakView.get().showTrailers();
                            weakView.get().refreshTrailers(videos);
                        } else {
                            weakView.get().hideTrailers();
                        }
                    }
                }));
        disposable.add(repository.getReviews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Review>>() {
                    @Override
                    public void accept(List<Review> reviews) {
                        if (reviews != null && reviews.size() > 0) {
                            weakView.get().showReviews();
                            weakView.get().refreshReviews(reviews);
                        } else {
                            weakView.get().hideReviews();
                        }
                    }
                }));
    }

    @Override
    public void onFavoritePressed() {
        disposable.add(repository.changeFavoriteState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(Boolean aBoolean) {
                                   weakView.get().showFloatingButton(aBoolean);
                               }
                           }));
    }

    @Override
    public void onVideoPressed(int position) {
        String url = repository.getTrailerUrl(position);
        weakView.get().goToYoutube(url);
    }
}
