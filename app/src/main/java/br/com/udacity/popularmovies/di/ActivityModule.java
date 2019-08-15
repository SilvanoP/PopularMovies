package br.com.udacity.popularmovies.di;

import br.com.udacity.popularmovies.feature.listmovies.ListMoviesModule;
import br.com.udacity.popularmovies.feature.listmovies.MainActivity;
import br.com.udacity.popularmovies.feature.moviedetail.MovieDetailModule;
import br.com.udacity.popularmovies.feature.moviedetail.MovieDetailActivity;
import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = AndroidInjectionModule.class)
public abstract class ActivityModule {

    @ScopeActivity
    @ContributesAndroidInjector(modules = ListMoviesModule.class)
    public abstract MainActivity contributesMainActivity();

    @ScopeActivity
    @ContributesAndroidInjector(modules = MovieDetailModule.class)
    public abstract MovieDetailActivity contributesMovieDetailActivity();
}
