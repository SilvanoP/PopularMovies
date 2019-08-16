package br.com.udacity.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static String formatDate(String date) {
        String[] segments = date.split("-");
        String formattedDate = segments[2] + "/" + segments[1] + "/" + segments[0];
        return formattedDate;
    }

    public static String dateToString(Date date, String mask) {
        return new SimpleDateFormat(mask, Locale.getDefault()).format(date);
    }

}
