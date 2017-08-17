package br.com.udacity.popularmovies.view;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.udacity.popularmovies.BuildConfig;
import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.database.MovieContract;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.model.Review;
import br.com.udacity.popularmovies.model.ReviewsListResponse;
import br.com.udacity.popularmovies.model.Video;
import br.com.udacity.popularmovies.model.VideosListResponse;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.TheMovieDBClient;
import br.com.udacity.popularmovies.util.tasks.AsyncTaskCallback;
import br.com.udacity.popularmovies.util.tasks.GetMoviesFromLocalDBAsyncTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    @BindView(R.id.movie_detail_favorite_image)
    ImageView mIsFavoriteImage;
    @BindView(R.id.movie_detail_trailers_list)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.movie_detail_reviews_list)
    RecyclerView mReviewsRecyclerView;

    private Movie mMovie;
    private List<Review> mReviews;
    private List<Video> mTrailers;

    private ListTrailersAdapter mTrailerAdapter;
    private ListTrailersAdapter.ListTrailerClickListener mListener;
    private ListReviewsAdapter mReviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();

        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListener = new ListTrailersAdapter.ListTrailerClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                Video trailer = mTrailers.get(clickedItemIndex);
                String url = YOUTUBE_BASE_URL + trailer.getKey();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage(YOUTUBE_PACKAGE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mIsFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie.isFavorite()) {
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(String.valueOf(mMovie.getId())).build();
                    Log.d(MovieDetailActivity.class.getSimpleName(), "Uri to delete: " + uri);
                    int numDeleted = 0;
                    try {
                        numDeleted = getContentResolver().delete(uri, null, null);
                    } catch (UnsupportedOperationException e) {
                        e.printStackTrace();
                    }

                    if (numDeleted > 0) {
                        mMovie.setFavorite(false);
                        mIsFavoriteImage.setImageResource(R.drawable.ic_not_favorite);
                    }
                    Log.i(MovieDetailActivity.class.getSimpleName(), "Deleted " + numDeleted + " rows!");
                } else {
                    mMovie.setFavorite(true);
                    String releaseDate = new SimpleDateFormat("yyyy-MM-dd").format(mMovie.getReleaseDate());

                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry._ID, mMovie.getId());
                    values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, mMovie.getBackdropUrl());
                    values.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, mMovie.getPosterUrl());
                    values.put(MovieContract.MovieEntry.COLUMN_NAME, mMovie.getName());
                    values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_NAME, mMovie.getOriginalName());
                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                    values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
                    values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
                    Uri savedUri = Uri.EMPTY;
                    try {
                        savedUri = getContentResolver().insert(uri, values);
                    } catch (UnsupportedOperationException e) {
                        e.printStackTrace();
                    }
                    if (savedUri != Uri.EMPTY) {
                        mIsFavoriteImage.setImageResource(R.drawable.ic_is_favorite);
                    } else { //if it was not saved then keep favorite as false
                        mMovie.setFavorite(false);
                    }
                    Log.i(MovieDetailActivity.class.getSimpleName(), "Saved Uri: " + savedUri);
                }
            }
        });

        Intent i = getIntent();
        if (i != null && i.hasExtra(Constants.INTENT_EXTRA_MOVIE)) {
            mMovie = i.getParcelableExtra(Constants.INTENT_EXTRA_MOVIE);

            fillFields();
            verifyIfFavorite();
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

    private void fillTrailerList(List<Video> trailers) {
        mTrailerAdapter = new ListTrailersAdapter(this, trailers, mListener);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mTrailers = trailers;
    }

    private void fillReviewList(List<Review> reviews) {
        mReviewAdapter = new ListReviewsAdapter(this, reviews);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        mReviews = reviews;
    }

    private void verifyIfFavorite() {
        String uri = MovieContract.MovieEntry.CONTENT_URI.toString();
        String selection = "_id=?";
        String selectionArg = String.valueOf(mMovie.getId());
        String params[] = {uri, selection, selectionArg};
        new GetMoviesFromLocalDBAsyncTask(this, this).execute(params);
    }

    private TheMovieDBClient createClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TheMovieDBClient.class);
    }

    private void getTrailers() {
        String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;
        Call<VideosListResponse> call = createClient().getVideos(String.valueOf(mMovie.getId()), apiKey);
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
        String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;
        Call<ReviewsListResponse> call = createClient().getReviews(String.valueOf(mMovie.getId()), apiKey);
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
            mIsFavoriteImage.setImageResource(R.drawable.ic_is_favorite);
            mMovie.setFavorite(true);
        } else {
            mIsFavoriteImage.setImageResource(R.drawable.ic_not_favorite);
            mMovie.setFavorite(false);
        }
    }
}
