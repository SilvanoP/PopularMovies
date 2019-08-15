package br.com.udacity.popularmovies.feature.listmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import br.com.udacity.popularmovies.util.ItemClickListener;
import br.com.udacity.popularmovies.view.MovieDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ListMoviesContract.View,
        FilterDialog.SortDialogListener {


    @BindView(R.id.grid_recycle_view)
    RecyclerView mGridRecycleView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @Inject
    ListMoviesContract.Presenter presenter;

    private GridLayoutManager mLayoutManager;
    private GridMoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        init();

        presenter.loadData();
    }

    public void init() {
        presenter.setView(this);

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

        ItemClickListener listener = new ItemClickListener(){
            @Override
            public void onItemClick(Movie movie) {
                presenter.onMovieSelected(movie);
            }
        };

        mAdapter = new GridMoviesAdapter(this, new ArrayList<Movie>(), listener);
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
