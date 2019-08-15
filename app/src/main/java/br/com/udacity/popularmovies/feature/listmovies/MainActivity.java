package br.com.udacity.popularmovies.feature.listmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import br.com.udacity.popularmovies.feature.moviedetail.MovieDetailActivity;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements ListMoviesContract.View,
        FilterDialog.SortDialogListener, GridMoviesAdapter.GridAdapterClickListener {

    private ProgressBar mProgressBar;
    private RecyclerView mGridRecycleView;
    private Toolbar mToolbar;

    @Inject
    ListMoviesContract.Presenter presenter;

    private GridLayoutManager mLayoutManager;
    private GridMoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.grid_progress);
        mGridRecycleView = findViewById(R.id.grid_recycle_view);
        mToolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        init();

        presenter.setView(this);
        presenter.loadData();
    }

    public void init() {
        int numColumns = getResources().getInteger(R.integer.movies_columns);
        mLayoutManager = new GridLayoutManager(this, numColumns);
        mGridRecycleView.setLayoutManager(mLayoutManager);
        mGridRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int last = mLayoutManager.findLastVisibleItemPosition();
                presenter.onScrollList(last);
            }
        });

        mAdapter = new GridMoviesAdapter(this, new ArrayList<Movie>(), this);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(Movie movie) {
        presenter.onMovieSelected(movie);
    }

    @Override
    public void refreshMovieList(List<Movie> movies) {
        mAdapter.swapItems(movies);
        mGridRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void addMovies(List<Movie> movies) {
        mAdapter.addItems(movies);
    }

    @Override
    public void showSortDialog(MovieCategory category) {
        FilterDialog dialog = FilterDialog.newInstance(category);
        dialog.show(getSupportFragmentManager(), "SORT_DIALOG");
    }

    @Override
    public void sort(MovieCategory category) {
        presenter.changeCategory(category);
    }

    @Override
    public void goToMovieDetail() {
        Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        startActivity(movieDetailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_movies_item:
                presenter.onSortMenuSelected();
                return true;
            case R.id.refresh_movies_item:
                presenter.loadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
