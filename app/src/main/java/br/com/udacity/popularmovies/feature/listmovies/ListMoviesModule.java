package br.com.udacity.popularmovies.feature.listmovies;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ListMoviesModule {

    @Binds
    public abstract ListMoviesContract.Presenter providesPresenter(ListMoviesPresenter presenter);
}
