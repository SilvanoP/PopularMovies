package br.com.udacity.popularmovies.util.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.model.MoviesListResponse;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.TheMovieDBClient;
import br.com.udacity.popularmovies.util.tasks.AsyncTaskCallback;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetMoviesFromTheMovieDBAsyncTask extends AsyncTask<String, Integer, List<Movie>> {

    private AsyncTaskCallback mCallback;

    public GetMoviesFromTheMovieDBAsyncTask(AsyncTaskCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String endpoint = strings[0];
        String page = "1";
        if (strings.length > 1) {
            page = strings[1];
        }

        List<Movie> movies = new ArrayList<>();

        Gson gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build();

        String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;
        TheMovieDBClient client = retrofit.create(TheMovieDBClient.class);
        Call<MoviesListResponse> call = null;

        if (endpoint.equals(Constants.POPULAR_ENDPOINT)) {
            call = client.getPopularMovies(apiKey, Integer.valueOf(page));
        } else if (endpoint.equals(Constants.TOP_RATED_ENDPOINT)) {
            call = client.getTopRatedMovies(apiKey, Integer.valueOf(page));
        }

        if (call != null) {
            try {
                Response<MoviesListResponse> response = call.execute();
                MoviesListResponse movieList = response.body();
                if (movieList != null) {
                    movies = movieList.getMovies();
                } else {
                    Log.d("HTTP REQUEST", "Error: " + response.code());
                }
            } catch (IOException e) {
                Log.e("HTTP REQUEST", e.getMessage());
                e.printStackTrace();
            }
        }

        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        mCallback.onAsyncTaskComplete(movies);
    }
}
