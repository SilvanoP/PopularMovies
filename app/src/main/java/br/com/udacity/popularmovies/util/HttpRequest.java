package br.com.udacity.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    private static final String API_KEY = ""; //FIXME Insert the API_KEY before running
    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_PAGE = "page";

    public static InputStream getResponseFromTMDb(String endpoint) throws IOException {
        Log.d(HttpRequest.class.getSimpleName(), "STARTING GET MOVIES DATA");
        if (API_KEY.isEmpty()) {
            return null;
        }
        Uri builtUri = Uri.parse(MOVIES_BASE_URL + endpoint).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .appendQueryParameter(PARAM_PAGE, "1")
                .build();

        URL url = new URL(builtUri.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream response =  new BufferedInputStream(urlConnection.getInputStream());

        urlConnection.disconnect();

        Log.d(HttpRequest.class.getSimpleName(), "FINISHED GET MOVIES DATA");
        return response;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
