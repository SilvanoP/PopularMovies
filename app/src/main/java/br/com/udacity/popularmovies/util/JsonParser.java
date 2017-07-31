package br.com.udacity.popularmovies.util;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.udacity.popularmovies.model.Movie;

public class JsonParser {

    public static List<Movie> parseMoviesFromJson(InputStream json) throws IOException {
        List<Movie> movies = new ArrayList<>();
        if (json == null) {
            return movies;
        }

        try (JsonReader reader = new JsonReader(new InputStreamReader(json))) {
            reader.beginObject(); // begin root
            while (reader.hasNext()) { // goes through each node
                String nodeKey = reader.nextName();
                if (nodeKey.equals("results")) { // movies node
                    reader.beginArray();
                    while (reader.hasNext()) { // goes through each movie node
                        Movie m = getMovieFromJson(reader);
                        if (!m.isEmpty()) {
                            movies.add(m);
                        }
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }

        return movies;
    }

    private static Movie getMovieFromJson(JsonReader reader) throws IOException {
        Movie movie = new Movie();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    movie.setId(reader.nextLong());
                    break;
                case "vote_average":
                    movie.setVoteAverage((float)reader.nextDouble());
                    break;
                case "title":
                    movie.setName(reader.nextString());
                    break;
                case "original_title":
                    movie.setOriginalName(reader.nextString());
                    break;
                case "popularity":
                    movie.setPopularity((float)reader.nextDouble());
                    break;
                case "overview":
                    movie.setOverview(reader.nextString());
                    break;
                case "release_date":
                    String date = reader.nextString();
                    String dateParser[] = date.split("-");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, Integer.valueOf(dateParser[0]));
                    cal.set(Calendar.MONTH, Integer.valueOf(dateParser[1]));
                    cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateParser[2]));
                    movie.setReleaseDate(cal);
                    break;
                case "poster_path":
                    movie.setPosterUrl(reader.nextString());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return movie;
    }
}
