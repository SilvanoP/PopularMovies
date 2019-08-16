package br.com.udacity.popularmovies.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.udacity.popularmovies.data.entities.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class PopularMoviesDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
}
