package br.com.udacity.popularmovies.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.model.Review;
import br.com.udacity.popularmovies.model.ReviewsListResponse;
import br.com.udacity.popularmovies.model.Video;
import br.com.udacity.popularmovies.model.VideosListResponse;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.ItemClickListener;
import br.com.udacity.popularmovies.util.Utils;
import br.com.udacity.popularmovies.util.tasks.AsyncTaskCallback;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import br.com.udacity.popularmovies.service.MovieService;

public class MovieDetailActivity extends AppCompatActivity implements AsyncTaskCallback {

    private final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private final String YOUTUBE_PACKAGE = "com.google.android.youtube";

    @BindView(R.id.movie_detail_backdrop_image)
    ImageView mBackdropImageView;
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
    @BindView(R.id.movie_detail_favorite_button)
    FloatingActionButton mIsFavoriteButton;
    @BindView(R.id.movie_detail_trailers_list)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.movie_detail_reviews_list)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.empty_trailers_text)
    TextView mEmptyTrailerTextView;
    @BindView(R.id.empty_reviews_text)
    TextView mEmptyReviewTextView;
    @BindView(R.id.movie_detail_release_date_text)
    TextView mReleaseDateTextView;

    private Movie mMovie;
    private MovieService service;
    private List<Review> mReviews;
    private List<Video> mTrailers;
    private ItemClickListener mTrailerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();
        service = new MovieService(this);

        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailerListener = new ItemClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {
                Video trailer = mTrailers.get(clickedItemIndex);
                String url = YOUTUBE_BASE_URL + trailer.getKey();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage(YOUTUBE_PACKAGE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mIsFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie.isFavorite()) {
                    if (service.removeMovieFromFavorite(String.valueOf(mMovie.getId()))) {
                        mMovie.setFavorite(false);
                        mIsFavoriteButton.setImageResource(R.drawable.ic_not_favorite);
                    }
                } else {
                    mMovie.setFavorite(true);

                    if (service.saveMovieToFavorite(mMovie)) {
                        mIsFavoriteButton.setImageResource(R.drawable.ic_is_favorite);
                    } else { //if it was not saved then keep favorite as false
                        mMovie.setFavorite(false);
                    }
                }
            }
        });

        Intent i = getIntent();
        if (i != null && i.hasExtra(Constants.INTENT_EXTRA_MOVIE)) {
            mMovie = i.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);

            fillFields();
            service.verifyMovieFavorite(this, String.valueOf(mMovie.getId()));
            if (mTrailers.size() > 0) {
                fillTrailerList(mTrailers);
            } else {
                getTrailers();
            }
            if (mReviews.size() > 0) {
                fillReviewList(mReviews);
            } else {
                getReviews();
            }
        } else {
            Toast.makeText(this, R.string.error_movie_not_found, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void fillFields() {
        if (!mMovie.isEmpty()) {
            String backdropSize = getResources().getString(R.string.backdrop_image_size);
            String backdropUrl = Constants.IMAGE_BASE_URL + backdropSize + mMovie.getBackdropUrl();
            Picasso.with(this)
                    .load(backdropUrl)
                    .placeholder(R.drawable.error_backdrop_image)
                    .error(R.drawable.error_backdrop_image)
                    .into(mBackdropImageView);

            String imageSize = "w" + String.valueOf(getResources().getInteger(R.integer.movie_detail_poster_size));
            String posterUrl = Constants.IMAGE_BASE_URL + imageSize + mMovie.getPosterUrl();
            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.drawable.error_loading_image)
                    .error(R.drawable.error_loading_image)
                    .into(mPosterImageView);
            float scoreMax5 = (mMovie.getVoteAverage() * 5)/10;
            String releaseDate = getResources().getString(R.string.release_date) + ": " +
                    Utils.dateToString(mMovie.getReleaseDate(),"MM/dd/yyyy");

            mNameTextView.setText(mMovie.getName());
            mReleaseDateTextView.setText(releaseDate);
            mOriginalNameTextView.setText(mMovie.getOriginalName());
            mScoreRatingTextView.setText(String.valueOf(mMovie.getVoteAverage()));
            mRatingBar.setRating(scoreMax5);
            mOverviewTextView.setText(mMovie.getOverview());
        }

    }

    private void fillTrailerList(List<Video> trailers) {
        if (trailers.size() == 0) {
            mTrailerRecyclerView.setVisibility(View.GONE);
            mEmptyTrailerTextView.setVisibility(View.VISIBLE);
        } else {
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTrailerTextView.setVisibility(View.GONE);

            ListTrailersAdapter trailerAdapter = new ListTrailersAdapter(this, trailers, mTrailerListener);
            mTrailerRecyclerView.setAdapter(trailerAdapter);
        }
        mTrailers = trailers;
    }

    private void fillReviewList(List<Review> reviews) {
        if (reviews.size() == 0) {
            mReviewsRecyclerView.setVisibility(View.GONE);
            mEmptyReviewTextView.setVisibility(View.VISIBLE);
        } else {
            mReviewsRecyclerView.setVisibility(View.VISIBLE);
            mEmptyReviewTextView.setVisibility(View.GONE);

            ListReviewsAdapter reviewAdapter = new ListReviewsAdapter(this, reviews);
            mReviewsRecyclerView.setAdapter(reviewAdapter);
        }
        mReviews = reviews;
    }

    private void getTrailers() {
        Call<VideosListResponse> call = service.getMovieTrailers(String.valueOf(mMovie.getId()));
        if (call != null) {
            call.enqueue(new Callback<VideosListResponse>() {
                @Override
                public void onResponse(Call<VideosListResponse> call, Response<VideosListResponse> response) {
                    VideosListResponse videosList = response.body();
                    if (videosList != null) {
                        fillTrailerList(videosList.getVideos());
                    }
                }

                @Override
                public void onFailure(Call<VideosListResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getReviews() {
        Call<ReviewsListResponse> call = service.getMovieReviews(String.valueOf(mMovie.getId()));
        if (call != null) {
            call.enqueue(new Callback<ReviewsListResponse>() {
                @Override
                public void onResponse(Call<ReviewsListResponse> call, Response<ReviewsListResponse> response) {
                    ReviewsListResponse reviewsList = response.body();
                    if (reviewsList != null) {
                        fillReviewList(reviewsList.getReviews());
                    }
                }

                @Override
                public void onFailure(Call<ReviewsListResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onAsyncTaskComplete(List<Movie> movies) {
        if (movies.size() == 1) {
            mIsFavoriteButton.setImageResource(R.drawable.ic_is_favorite);
            mMovie.setFavorite(true);
        } else {
            mIsFavoriteButton.setImageResource(R.drawable.ic_not_favorite);
            mMovie.setFavorite(false);
        }
    }
}
