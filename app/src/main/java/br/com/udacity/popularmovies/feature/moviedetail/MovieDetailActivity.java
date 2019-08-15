package br.com.udacity.popularmovies.feature.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Review;
import br.com.udacity.popularmovies.data.entities.Video;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.ItemClickListener;
import dagger.android.AndroidInjection;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    private static final String YOUTUBE_PACKAGE = "com.google.android.youtube";

    private ImageView mBackdropImageView;
    private ImageView mPosterImageView;
    private TextView mNameTextView;
    private TextView mOriginalNameTextView;
    private TextView mOverviewTextView;
    private TextView mScoreRatingTextView;
    private RatingBar mRatingBar;
    private FloatingActionButton mIsFavoriteButton;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private TextView mEmptyTrailerTextView;
    private TextView mEmptyReviewTextView;
    private TextView mReleaseDateTextView;

    @Inject
    MovieDetailContract.Presenter presenter;

    private ItemClickListener mTrailerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        init();
        presenter.setView(this);
        presenter.loadData();
    }

    private void init() {
        mBackdropImageView = findViewById(R.id.movie_detail_backdrop_image);
        mPosterImageView = findViewById(R.id.movie_detail_poster_image);
        mNameTextView = findViewById(R.id.movie_detail_name_text);
        mOriginalNameTextView = findViewById(R.id.movie_detail_original_name_text);
        mOverviewTextView = findViewById(R.id.movie_detail_overview_text);
        mScoreRatingTextView = findViewById(R.id.movie_detail_score_rating_text);
        mRatingBar = findViewById(R.id.movie_detail_score_rating);
        mIsFavoriteButton = findViewById(R.id.movie_detail_favorite_button);
        mTrailerRecyclerView = findViewById(R.id.movie_detail_trailers_list);
        mReviewsRecyclerView = findViewById(R.id.movie_detail_reviews_list);
        mEmptyTrailerTextView = findViewById(R.id.empty_trailers_text);
        mEmptyReviewTextView = findViewById(R.id.empty_reviews_text);
        mReleaseDateTextView = findViewById(R.id.movie_detail_release_date_text);

        mIsFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFavoritePressed();
            }
        });

        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerListener = new ItemClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {
                presenter.onVideoPressed(clickedItemIndex);
            }
        };

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showBackdropImage(String backdropUrl) {
        String backdropSize = getResources().getString(R.string.backdrop_image_size);
        String backdropFullUrl = Constants.IMAGE_BASE_URL + backdropSize + backdropUrl;
        Picasso.get()
                .load(backdropFullUrl)
                .placeholder(R.drawable.error_backdrop_image)
                .error(R.drawable.error_backdrop_image)
                .into(mBackdropImageView);
    }

    @Override
    public void showPosterImage(String posterUrl) {
        String imageSize = "w" + (getResources().getInteger(R.integer.movie_detail_poster_size));
        String posterFullUrl = Constants.IMAGE_BASE_URL + imageSize + posterUrl;
        Picasso.get()
                .load(posterFullUrl)
                .placeholder(R.drawable.error_loading_image)
                .error(R.drawable.error_loading_image)
                .into(mPosterImageView);
    }

    @Override
    public void showTitle(String title) {
        mNameTextView.setText(title);
    }

    @Override
    public void showOriginalName(String originalName) {
        mOriginalNameTextView.setText(originalName);
    }

    @Override
    public void showReleaseDate(String releaseDate) {
        String releaseDateField = getString(R.string.release_date) + ": " + releaseDate;
        mReleaseDateTextView.setText(releaseDateField);
    }

    @Override
    public void showRating(float average) {
        float scoreMax5 = (average * 5)/10;
        mScoreRatingTextView.setText(String.valueOf(average));
        mRatingBar.setRating(scoreMax5);
    }

    @Override
    public void showOverview(String overview) {
        mOverviewTextView.setText(overview);
    }

    @Override
    public void showTrailers() {
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTrailerTextView.setVisibility(View.GONE);
    }

    @Override
    public void hideTrailers() {
        mTrailerRecyclerView.setVisibility(View.GONE);
        mEmptyTrailerTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshTrailers(List<Video> videos) {
        ListTrailersAdapter trailerAdapter = new ListTrailersAdapter(this, videos, mTrailerListener);
        mTrailerRecyclerView.setAdapter(trailerAdapter);
    }

    @Override
    public void showReviews() {
        mReviewsRecyclerView.setVisibility(View.VISIBLE);
        mEmptyReviewTextView.setVisibility(View.GONE);
    }

    @Override
    public void hideReviews() {
        mReviewsRecyclerView.setVisibility(View.GONE);
        mEmptyReviewTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshReviews(List<Review> reviews) {
        ListReviewsAdapter reviewAdapter = new ListReviewsAdapter(this, reviews);
        mReviewsRecyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void changeFavoriteIcon(boolean isFavorited) {
        if (isFavorited) {
            mIsFavoriteButton.setImageResource(R.drawable.ic_is_favorite);
        } else {
            mIsFavoriteButton.setImageResource(R.drawable.ic_not_favorite);
        }
    }

    @Override
    public void goToYoutube(String youtubeUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(YOUTUBE_PACKAGE);
        intent.setData(Uri.parse(youtubeUrl));
        startActivity(intent);
    }
}
