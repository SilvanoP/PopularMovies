package br.com.udacity.popularmovies.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.data.database.PopularMoviesDatabase;
import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.data.entities.remote.MoviesListResponse;
import br.com.udacity.popularmovies.data.webservice.CacheInterceptor;
import br.com.udacity.popularmovies.data.webservice.TheMovieDBClient;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.Utils;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class MoviesRepositoryImpl implements MoviesRepository {

    private final int NUM_MOVIES_PER_PAGE = 20;

    private Context context;
    private CacheInterceptor interceptor;
    private SharedPreferences preferences;
    private TheMovieDBClient client;
    private PopularMoviesDatabase database;
    private String tmdbApi;

    private MovieCategory currentCategory;
    private int currentPage;
    private Movie selectedMovie;

    @Inject
    public MoviesRepositoryImpl(Context context, SharedPreferences preferences, TheMovieDBClient client,
                                PopularMoviesDatabase database, CacheInterceptor interceptor) {
        this.context = context;
        this.interceptor = interceptor;
        this.preferences = preferences;
        this.client = client;
        this.database = database;
        currentPage = 1;
        tmdbApi = BuildConfig.THE_MOVIE_DB_API_KEY;

        currentCategory = initMovieCategory();
    }

    private MovieCategory initMovieCategory() {
        int categoryValue = preferences.getInt(Constants.PREFERENCE_SORT_MOVIES, MovieCategory.POPULAR.getValue());
        for (MovieCategory category : MovieCategory.values()) {
            if (category.getValue() == categoryValue) {
                return category;
            }
        }
        return null;
    }

    @Override
    public Single<List<Movie>> getMoviesFromCategory() {
        interceptor.setOnline(Utils.isOnline(context));

        switch (currentCategory) {
            case POPULAR:
                return getPopularMovies();
            case FAVORITE:
                return getFavoriteMovies();
            case UPCOMING:
                return getUpcomingMovies();
            case TOP_RATED:
                return getTopRatedMovies();
        }
        return Single.error(new IllegalArgumentException("Category not found"));
    }

    @Override
    public Single<List<Movie>> loadNextPage(int position) {
        if (position >= ((NUM_MOVIES_PER_PAGE-1) * currentPage)) {
            currentPage++;
            return getMoviesFromCategory();
        }

        return Single.just((List<Movie>)new ArrayList<Movie>());
    }

    private Single<List<Movie>> getPopularMovies() {
        return client.getPopularMovies(tmdbApi, currentPage).flatMap(new Function<MoviesListResponse, SingleSource<List<Movie>>>() {
            @Override
            public SingleSource<List<Movie>> apply(MoviesListResponse moviesListResponse) {
                return Single.just(moviesListResponse.getMovies());
            }
        });
    }

    private Single<List<Movie>> getTopRatedMovies() {
        return client.getTopRatedMovies(tmdbApi, currentPage).flatMap(new Function<MoviesListResponse, SingleSource<List<Movie>>>() {
            @Override
            public SingleSource<List<Movie>> apply(MoviesListResponse moviesListResponse) {
                return Single.just(moviesListResponse.getMovies());
            }
        });
    }

    private Single<List<Movie>> getUpcomingMovies() {
        return client.getUpcomingMovies(tmdbApi, currentPage).flatMap(new Function<MoviesListResponse, SingleSource<List<Movie>>>() {
            @Override
            public SingleSource<List<Movie>> apply(MoviesListResponse moviesListResponse) {
                return Single.just(moviesListResponse.getMovies());
            }
        });
    }

    private Single<List<Movie>> getFavoriteMovies() {
        return database.movieDAO().getAll();
    }

    @Override
    public MovieCategory getCategory() {
        return currentCategory;
    }

    @Override
    public void changeCategory(MovieCategory category) {
        currentCategory = category;
        currentPage = 1;
    }

    @Override
    public void setSelectedMovie(Movie movie) {
        this.selectedMovie = movie;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }
}
