package br.com.udacity.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.model.Movie;

public class Utils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String dateToString(Date date) {
        String mask = "yyyy-MM-dd"; // default mask for this app
        return dateToString(date, mask);
    }

    public static String dateToString(Date date, String mask) {
        return new SimpleDateFormat(mask).format(date);
    }

}
