package br.com.udacity.popularmovies.feature.listmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.remote.MoviesListResponse;
import br.com.udacity.popularmovies.util.ItemClickListener;
import br.com.udacity.popularmovies.util.tasks.AsyncTaskCallback;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.Utils;
import br.com.udacity.popularmovies.view.MovieDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import br.com.udacity.popularmovies.service.MovieService;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallback, ListMoviesContract.View {

    private final String CURRENT_MOVIE_PAGE_KEY = "current_page";
    private final String MOVIE_LIST_KEY = "movie_list";
    private final int NUM_MOVIES_PER_PAGE = 20;

    @BindView(R.id.grid_recycle_view)
    RecyclerView mGridRecycleView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    private SharedPreferences mPreferences;
    private GridLayoutManager mLayoutManager;
    private GridMoviesAdapter mAdapter;
    private ItemClickListener mListener;
    private MovieService mService;
    private List<Movie> mMovies;
    private int mCurrentMoviePage; // the movie database page to load
    private boolean mLoadMoreMovies;
    private String mCurrentSortOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mService = new MovieService(this);

        mPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        mCurrentSortOption = mPreferences.getString(Constants.PREFERENCE_SORT_MOVIES, Constants.POPULAR_ENDPOINT );
        int numColumns = getResources().getInteger(R.integer.movies_columns);

        mLayoutManager = new GridLayoutManager(this, numColumns);
        mGridRecycleView.setLayoutManager(mLayoutManager);
        mGridRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int last = mLayoutManager.findLastVisibleItemPosition();
                if (last >= ((NUM_MOVIES_PER_PAGE-1) * mCurrentMoviePage)) {
                    mLoadMoreMovies = true;
                    mCurrentMoviePage++;
                    runGetMoviesFromTheMovieDBTask();
            }
        }
        });

        mListener = new ItemClickListener(){
            @Override
            public void onItemClick(int clickedItemIndex) {
                Movie m = mMovies.get(clickedItemIndex);
                Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                movieDetailIntent.putExtra(Constants.INTENT_EXTRA_MOVIE, m);
                startActivity(movieDetailIntent);
            }
        };

        mCurrentMoviePage = 1; //first page
        mLoadMoreMovies = false;

        if (!mCurrentSortOption.equals(Constants.FAVORITE_ENDPOINT)) {
            if(savedInstanceState != null) {
                mCurrentMoviePage = savedInstanceState.getInt(CURRENT_MOVIE_PAGE_KEY, 1);
                mMovies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
                refreshMoviesGrid();
            } else {
                runGetMoviesFromTheMovieDBTask();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Only the list from database needs to be updated on onResume, since it was keeping
        // the movies even after thee user 'unfavorited' it on detail screen and pressed back
        if (mCurrentSortOption.equals(Constants.FAVORITE_ENDPOINT)) {
            mService.getMoviesFromLocalDb(this);
        }
    }

    @Override
    public void refreshMovieList(List<Movie> movies) {

    }

    @Override
    public void goToMovieDetail() {

    }

    private void runGetMoviesFromTheMovieDBTask() {
        if (!Utils.isOnline(this)) {
            Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
        } else {
            Call<MoviesListResponse> call = mService.getMoviesFromWeb(mCurrentSortOption,
                    String.valueOf(mCurrentMoviePage));

            call.enqueue(new Callback<MoviesListResponse>() {
                @Override
                public void onResponse(Call<MoviesListResponse> call, Response<MoviesListResponse> response) {
                    MoviesListResponse movieList = response.body();
                    if (movieList != null) {
                        receiveMovieListFromWeb(movieList.getMovies());
                    }
                }

                @Override
                public void onFailure(Call<MoviesListResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void receiveMovieListFromWeb(List<Movie> movies) {
        if (mLoadMoreMovies) {
            mMovies.addAll(movies);
            mAdapter.addItems(movies);
            mLoadMoreMovies = false;
        } else {
            mMovies = movies;
            refreshMoviesGrid();
        }
    }

    private void refreshMoviesGrid() {
        if (mMovies.size() == 0) {
            Toast.makeText(this, R.string.error_loading_list, Toast.LENGTH_LONG).show();
        }

        if (mAdapter == null) {
            mAdapter = new GridMoviesAdapter(this, mMovies, mListener);
        } else {
            mAdapter.swapItems(mMovies);
        }
        mGridRecycleView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        switch (mCurrentSortOption) {
            case Constants.POPULAR_ENDPOINT:
                MenuItem popularMenuItem = menu.findItem(R.id.popular_menu_item);
                popularMenuItem.setChecked(true);
                break;
            case Constants.TOP_RATED_ENDPOINT:
                MenuItem topRatedMenuItem = menu.findItem(R.id.top_rated_menu_item);
                topRatedMenuItem.setChecked(true);
                break;
            case Constants.FAVORITE_ENDPOINT:
                MenuItem favoriteMenuItem = menu.findItem(R.id.favorite_menu_item);
                favoriteMenuItem.setChecked(true);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_menu_item:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mCurrentSortOption = Constants.POPULAR_ENDPOINT;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(Constants.PREFERENCE_SORT_MOVIES, mCurrentSortOption);
                    editor.apply();

                    mCurrentMoviePage = 1; // reset the movie page that will be loaded
                    runGetMoviesFromTheMovieDBTask();
                }
                return true;
            case R.id.top_rated_menu_item:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mCurrentSortOption = Constants.TOP_RATED_ENDPOINT;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(Constants.PREFERENCE_SORT_MOVIES, mCurrentSortOption);
                    editor.apply();

                    mCurrentMoviePage = 1; // reset the movie page that will be loaded
                    runGetMoviesFromTheMovieDBTask();
                }
                return true;
            case R.id.refresh_movies_item:
                mCurrentMoviePage = 1; // reset the movie page that will be loaded
                if (mCurrentSortOption.equals(Constants.FAVORITE_ENDPOINT)) {
                    mService.getMoviesFromLocalDb(this);
                } else {
                    runGetMoviesFromTheMovieDBTask();
                }
                return true;
            case R.id.favorite_menu_item:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    mCurrentSortOption = Constants.FAVORITE_ENDPOINT;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(Constants.PREFERENCE_SORT_MOVIES, mCurrentSortOption);
                    editor.apply();

                    mCurrentMoviePage = 1; // reset the movie page that will be loaded
                    mService.getMoviesFromLocalDb(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_MOVIE_PAGE_KEY, mCurrentMoviePage);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, new ArrayList<Parcelable>(mMovies));

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAsyncTaskComplete(List<Movie> movies) {
        mMovies = movies;
        refreshMoviesGrid();
    }
}
