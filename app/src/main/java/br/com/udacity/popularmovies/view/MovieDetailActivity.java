package br.com.udacity.popularmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.movie_detail_poster_image)
    ImageView mPosterImageView;
    @BindView(R.id.movie_detail_name_text)
    TextView mNameTextView;
    @BindView(R.id.movie_detail_original_name_text)
    TextView mOriginalNameTextView;
    @BindView(R.id.movie_detail_overview_text)
    TextView mOverviewTextView;
    @BindView(R.id.movie_detail_score_rating_text)
    TextView mScoreRatingTextView;
    @BindView(R.id.movie_detail_score_rating)
    RatingBar mRatingBar;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        if (i != null && i.hasExtra(Constants.INTENT_EXTRA_MOVIE)) {
            mMovie = i.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);

            fillFields();
        }
    }

    private void fillFields() {
        if (!mMovie.isEmpty()) {
            String imageSize = "w" + String.valueOf(getResources().getInteger(R.integer.movie_detail_poster_size));
            Log.d(this.getClass().getSimpleName(), "Poster size:" + imageSize);
            String url = Constants.POSTER_BASE_URL + imageSize + mMovie.getPosterUrl();
            Picasso.with(this)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.error_loading_image)
                    .into(mPosterImageView);

            SimpleDateFormat releaseDateMask = new SimpleDateFormat("(yyyy)");
            Date releaseDate = mMovie.getReleaseDate();
            String movieTitle = mMovie.getName();
            if (releaseDate != null) {
                movieTitle += " " + releaseDateMask.format(releaseDate);
            }
            float scoreMax5 = (mMovie.getVoteAverage() * 5)/10;
            mNameTextView.setText(movieTitle);
            mOriginalNameTextView.setText(mMovie.getOriginalName());
            mScoreRatingTextView.setText(String.valueOf(mMovie.getVoteAverage()));
            mRatingBar.setRating(scoreMax5);
            mOverviewTextView.setText(mMovie.getOverview());
        }
    }
}
