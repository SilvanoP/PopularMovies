package br.com.udacity.popularmovies.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.model.Movie;

/**
 * Created by Silvano Damasceno on 05/08/2017.
 */

public class GetMoviesFromTMDbTask extends AsyncTask<String, Integer, List<Movie>> {

    private AsyncTaskCallback mCallback;

    public GetMoviesFromTMDbTask(AsyncTaskCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        List<Movie> movies = new ArrayList<>();

        try {
            String endpoint = strings[0];
            InputStream response = HttpRequest.getResponseFromTMDb(endpoint);
            movies = JsonParser.parseMoviesFromJson(response);
        } catch (Exception e) {
            Log.d("MainActivity", "Error response or JSON");
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        mCallback.onAsyncTaskComplete(movies);
    }
}
