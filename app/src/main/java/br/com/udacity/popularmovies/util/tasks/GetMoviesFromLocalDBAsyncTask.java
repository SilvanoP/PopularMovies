package br.com.udacity.popularmovies.util.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.udacity.popularmovies.database.MovieContract;
import br.com.udacity.popularmovies.model.Movie;

public class GetMoviesFromLocalDBAsyncTask extends AsyncTask<String, Integer, List<Movie>> {

    private Context mContext;
    private AsyncTaskCallback mCallback;

    public GetMoviesFromLocalDBAsyncTask (Context context, AsyncTaskCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        List<Movie> movies = new ArrayList<>();
        Uri uri = Uri.parse(strings[0]);
        String selection = null;
        String selectionArgs[] = null;
        if (strings.length == 3) {
            // When searching for a specific movie
            selection = strings[1];
            selectionArgs = new String[]{strings[2]};
        }

        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
                movie.setFavorite(true); // if it is on database then it is a favorite
                movie.setName(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME)));
                movie.setOriginalName(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_NAME)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setPosterUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL)));
                movie.setBackdropUrl(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_URL)));
                movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                String dateString = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                    movie.setReleaseDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movies.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        mCallback.onAsyncTaskComplete(movies);
    }
}
