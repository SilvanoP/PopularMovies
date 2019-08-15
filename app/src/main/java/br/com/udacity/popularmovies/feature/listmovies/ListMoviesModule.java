package br.com.udacity.popularmovies.feature.listmovies;

import dagger.Module;
import dagger.Provides;

@Module
public class ListMoviesModule {

    @Provides
    ListMoviesContract.Presenter providesPresenter(ListMoviesPresenter presenter) {
        return presenter;
    }
}
