package br.com.udacity.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.udacity.popularmovies.model.Movie;

public class MovieDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDb.db";
    private static final int VERSION = 1;

    public MovieDAO(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String create_table = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID                  + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP_URL  + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER_URL    + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_NAME          + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_NAME + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW      + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE  + " REAL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE  + " TEXT);";

        sqLiteDatabase.execSQL(create_table);
        Log.i(this.getClass().getSimpleName(), "TABLE CREATED!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
