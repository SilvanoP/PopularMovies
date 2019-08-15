package br.com.udacity.popularmovies.service;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.database.MovieContract;
import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.remote.MoviesListResponse;
import br.com.udacity.popularmovies.data.entities.remote.ReviewsListResponse;
import br.com.udacity.popularmovies.data.entities.remote.VideosListResponse;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.data.webservice.TheMovieDBClient;
import br.com.udacity.popularmovies.util.Utils;
import br.com.udacity.popularmovies.util.tasks.AsyncTaskCallback;
import br.com.udacity.popularmovies.util.tasks.GetMoviesFromLocalDBAsyncTask;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieService {

    private final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;

    private TheMovieDBClient client;
    private Context mContext;

    public MovieService(Context context) {
        mContext = context;

        Gson gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();

        client = retrofit.create(TheMovieDBClient.class);
    }

    public void getMoviesFromLocalDb(AsyncTaskCallback callback) {
        String params[] = {MovieContract.MovieEntry.CONTENT_URI.toString()};
        new GetMoviesFromLocalDBAsyncTask(mContext, callback).execute(params);
    }

    public void verifyMovieFavorite(AsyncTaskCallback callback, String movieId) {
        String uri = MovieContract.MovieEntry.CONTENT_URI.toString();
        String params[] = {uri, "_id=?", movieId};
        new GetMoviesFromLocalDBAsyncTask(mContext, callback).execute(params);
    }

    public Call<MoviesListResponse> getMoviesFromWeb(String endpoint, String page) {
        Call<MoviesListResponse> call;

        switch (endpoint) {
            case Constants.POPULAR_ENDPOINT:
                call = client.getPopularMovies(apiKey, Integer.valueOf(page));
                break;
            case Constants.TOP_RATED_ENDPOINT:
                call = client.getTopRatedMovies(apiKey, Integer.valueOf(page));
                break;
            default:
                Log.w(this.getClass().getSimpleName(), "Endpoint not valid for this search!");
                return null;
        }

        return call;
    }

    public Call<ReviewsListResponse> getMovieReviews(String movieId) {
        return client.getReviews(movieId, apiKey);
    }

    public Call<VideosListResponse> getMovieTrailers(String movieId) {
        return client.getVideos(movieId, apiKey);
    }

    public boolean saveMovieToFavorite(Movie movie) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, movie.getBackdropUrl());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, movie.getPosterUrl());
        values.put(MovieContract.MovieEntry.COLUMN_NAME, movie.getName());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_NAME, movie.getOriginalName());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, Utils.dateToString(movie.getReleaseDate()));
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        Uri savedUri = Uri.EMPTY;
        try {
            savedUri = mContext.getContentResolver().insert(uri, values);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }

        Log.d(this.getClass().getSimpleName(), "Saved uri: " + savedUri);
        return (savedUri!= null && !Uri.EMPTY.equals(savedUri));
    }

    public boolean removeMovieFromFavorite(String movieId) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();
        Log.d(this.getClass().getSimpleName(), "Uri to delete: " + uri);
        int numDeleted = 0;
        try {
            numDeleted = mContext.getContentResolver().delete(uri, null, null);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }

        return numDeleted > 0;
    }
}
