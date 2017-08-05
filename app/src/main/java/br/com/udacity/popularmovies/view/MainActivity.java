package br.com.udacity.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.util.AsyncTaskCallback;
import br.com.udacity.popularmovies.util.Constants;
import br.com.udacity.popularmovies.util.GetMoviesFromTMDbTask;
import br.com.udacity.popularmovies.util.HttpRequest;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallback {

    @BindView(R.id.grid_recycle_view)
    RecyclerView mGridRecycleView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    private GridMoviesAdapter mAdapter;

    private SharedPreferences mPreferences;
    private GridMoviesAdapter.GridItemClickListener mListener;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int numColumns = getResources().getInteger(R.integer.movies_columns);

        mGridRecycleView.setLayoutManager(new GridLayoutManager(this, numColumns));
        mListener = new GridMoviesAdapter.GridItemClickListener(){
            @Override
            public void onGridItemClick(int clickedItemIndex) {
                Movie m = mMovies.get(clickedItemIndex);
                Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                movieDetailIntent.putExtra(Constants.INTENT_EXTRA_MOVIE, m);
                startActivity(movieDetailIntent);
            }
        };

        runGetMoviesTask();
    }

    private void runGetMoviesTask() {
        if (!HttpRequest.isOnline(this)) {
            Log.d(this.getClass().getSimpleName(), "No Internet Connection");
            Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
        } else {
            String endpoint = mPreferences.getString(Constants.PREFERENCE_SORT_MOVIES, Constants.POPULAR_ENDPOINT);
            new GetMoviesFromTMDbTask(this).execute(endpoint);
        }
    }

    @Override
    public void onAsyncTaskComplete(List<Movie> movies) {
        mMovies = movies;
        refreshMoviesGrid();
    }

    private void refreshMoviesGrid() {
        if (mMovies.size() == 0) {
            Toast.makeText(this, R.string.error_loading_list, Toast.LENGTH_LONG).show();
        }

        mAdapter = new GridMoviesAdapter(this, mMovies, mListener);
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
        String endpoint = mPreferences.getString(Constants.PREFERENCE_SORT_MOVIES, Constants.POPULAR_ENDPOINT);
        if (endpoint.equals(Constants.POPULAR_ENDPOINT)) {
            MenuItem popularMenuItem = menu.findItem(R.id.popular_menu_item);
            popularMenuItem.setChecked(true);
        } else if (endpoint.equals(Constants.TOP_RATED_ENDPOINT)) {
            MenuItem topRatedMenuItem = menu.findItem(R.id.top_rated_meu_item);
            topRatedMenuItem.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_menu_item:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    String currentEndpoint = Constants.POPULAR_ENDPOINT;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(Constants.PREFERENCE_SORT_MOVIES, currentEndpoint);
                    editor.apply();

                    runGetMoviesTask();
                }
                return true;
            case R.id.top_rated_meu_item:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    String currentEndpoint = Constants.TOP_RATED_ENDPOINT;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(Constants.PREFERENCE_SORT_MOVIES, currentEndpoint);
                    editor.apply();

                    runGetMoviesTask();
                }
                return true;
            case R.id.refresh_movies_item:
                runGetMoviesTask();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
