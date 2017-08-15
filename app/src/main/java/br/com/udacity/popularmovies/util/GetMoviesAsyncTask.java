package br.com.udacity.popularmovies.util;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.model.MoviesListResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetMoviesAsyncTask extends AsyncTask<String, Integer, List<Movie>> {

    private AsyncTaskCallback mCallback;

    public GetMoviesAsyncTask(AsyncTaskCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String endpoint = strings[0];
        String page = "1";
        if (strings.length > 1) {
            page = strings[1];
        }

        if (endpoint.equals(Constants.FAVORITE_ENDPOINT)) {
            return getMovieFromLocalDB();
        } else {
            return getMoviesFromTheMovieDB(endpoint, page);
        }
    }

    private List<Movie> getMovieFromLocalDB() {
        List<Movie> movies = new ArrayList<>();
        // TODO complete this method with query from db
        return movies;
    }

    private List<Movie> getMoviesFromTheMovieDB(String endpoint, String page) {
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
