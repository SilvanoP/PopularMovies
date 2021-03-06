package br.com.udacity.popularmovies.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.data.database.PopularMoviesDatabase;
import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.data.entities.Review;
import br.com.udacity.popularmovies.data.entities.Video;
import br.com.udacity.popularmovies.data.entities.remote.MoviesListResponse;
import br.com.udacity.popularmovies.data.entities.remote.ReviewsListResponse;
import br.com.udacity.popularmovies.data.entities.remote.VideosListResponse;
import br.com.udacity.popularmovies.data.webservice.CacheInterceptor;
import br.com.udacity.popularmovies.data.webservice.TheMovieDBService;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.Utils;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MoviesRepositoryImpl implements MoviesRepository {

    private static final int NUM_MOVIES_PER_PAGE = 20;
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private Context context;
    private CacheInterceptor interceptor;
    private SharedPreferences preferences;
    private TheMovieDBService client;
    private PopularMoviesDatabase database;
    private String tmdbApi;

    private MovieCategory currentCategory;
    private int currentPage;
    private Movie selectedMovie;
    private List<Video> movieTrailers;

    public MoviesRepositoryImpl(Context context, SharedPreferences preferences, TheMovieDBService client,
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
        preferences.edit().putInt(Constants.PREFERENCE_SORT_MOVIES, category.getValue()).apply();
        currentCategory = category;
        currentPage = 1;
    }

    @Override
    public Maybe<List<Movie>> searchMovieByTitle(String title) {
        interceptor.setOnline(Utils.isOnline(context));
        Log.d(this.getClass().getSimpleName(), "Title: " + title);
        try {
            title = URLEncoder.encode(title, "UTF-8");
            Log.d(this.getClass().getSimpleName(), "Title encoded: " + title);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return client.getMoviesByTitle(tmdbApi, title, 1)
                .flatMap(new Function<MoviesListResponse, MaybeSource<List<Movie>>>() {
                    @Override
                    public MaybeSource<List<Movie>> apply(MoviesListResponse moviesListResponse) {
                        if (moviesListResponse == null) {
                            return Maybe.just((List<Movie>) new ArrayList<Movie>());
                        }
                        return Maybe.just(moviesListResponse.getMovies());
                    }
                });
    }

    @Override
    public void setSelectedMovie(Movie movie) {
        this.selectedMovie = movie;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    @Override
    public Maybe<Movie> isFavorite() {
        return database.movieDAO().findById(selectedMovie.getId());
    }

    @Override
    public Single<List<Video>> getTrailers() {
        interceptor.setOnline(Utils.isOnline(context));
        return client.getVideos(String.valueOf(selectedMovie.getId()), tmdbApi)
                .flatMap(new Function<VideosListResponse, SingleSource<? extends List<Video>>>() {
                    @Override
                    public SingleSource<? extends List<Video>> apply(VideosListResponse videosListResponse) throws Exception {
                        movieTrailers = videosListResponse.getVideos();
                        return Single.just(videosListResponse.getVideos());
                    }
                });
    }

    @Override
    public Single<List<Review>> getReviews() {
        interceptor.setOnline(Utils.isOnline(context));
        return client.getReviews(String.valueOf(selectedMovie.getId()), tmdbApi)
                .flatMap(new Function<ReviewsListResponse, SingleSource<? extends List<Review>>>() {
                    @Override
                    public SingleSource<? extends List<Review>> apply(ReviewsListResponse reviewsListResponse) throws Exception {
                        return Single.just(reviewsListResponse.getReviews());
                    }
                });
    }

    @Override
    public Single<Boolean> changeFavoriteState() {
        if (selectedMovie.isFavorite()) {
            return Single.fromCallable(new Callable<Boolean>() {
                @Override
                public Boolean call()  {
                    database.movieDAO().delete(selectedMovie);
                    selectedMovie.setFavorite(false);
                    return false;
                }
            });
        }

        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call()  {
                selectedMovie.setFavorite(true);
                database.movieDAO().insert(selectedMovie);
                return true;
            }
        });
    }

    @Override
    public String getTrailerUrl(int position) {
        if (movieTrailers != null && movieTrailers.size() > position) {
            return YOUTUBE_BASE_URL + movieTrailers.get(position).getKey();
        }

        return "";
    }
}
