package br.com.udacity.popularmovies.data.webservice;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    private boolean isOnline;

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (isOnline) {
            request = request.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 5)
                    .build();
        } else {
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                    .build();
        }

        Log.d(this.getClass().getSimpleName(), request.url().toString());
        Log.d(this.getClass().getSimpleName(), request.url().encodedQuery());
        return chain.proceed(request);
    }
}
