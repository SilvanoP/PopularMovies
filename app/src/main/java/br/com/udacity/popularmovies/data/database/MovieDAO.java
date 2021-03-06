package br.com.udacity.popularmovies.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Movie;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);
    @Delete
    void delete(Movie movie);
    @Query("SELECT * FROM movie")
    Single<List<Movie>> getAll();
    @Query("SELECT * FROM movie WHERE id = :id")
    Maybe<Movie> findById(Long id);
}
