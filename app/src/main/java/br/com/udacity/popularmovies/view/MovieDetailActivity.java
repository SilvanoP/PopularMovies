package br.com.udacity.popularmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.util.Constants;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mPosterImageView;
    private TextView mNameTextView;
    private TextView mOriginalNameTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private RatingBar mRatingBar;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent i = getIntent();
        mMovie = i.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);

        mPosterImageView = (ImageView) findViewById(R.id.movie_detail_poster_image);
        mNameTextView = (TextView) findViewById(R.id.movie_detail_name_text);
        mOriginalNameTextView = (TextView) findViewById(R.id.movie_detail_original_name_text);
        mReleaseDateTextView = (TextView) findViewById(R.id.movie_detail_release_date_text);
        mOverviewTextView = (TextView) findViewById(R.id.movie_detail_overview_text);
        mRatingBar = (RatingBar) findViewById(R.id.movie_detail_score_rating);

        fillFields();
    }

    private void fillFields() {
        if (!mMovie.isEmpty()) {
            String url = Constants.POSTER_BASE_URL + Constants.POSTER_LARGE_SIZE + mMovie.getPosterUrl();
            Picasso.with(this).load(url).into(mPosterImageView);

            mNameTextView.setText(mMovie.getName());
            mOriginalNameTextView.setText(mMovie.getOriginalName());
            Calendar releaseDate = mMovie.getReleaseDate();
            SimpleDateFormat releaseDateMask = new SimpleDateFormat("MM-dd-yyyy");
            mReleaseDateTextView.setText(releaseDateMask.format(releaseDate.getTimeInMillis()));
            mRatingBar.setRating(mMovie.getVoteAverage());
            mOverviewTextView.setText(mMovie.getOverview());
        }
    }
}
