package br.com.udacity.popularmovies.feature.moviedetail;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieDetailModule {

    @Provides
    MovieDetailContract.Presenter providesPresenter(MovieDetailPresenter presenter) {
        return presenter;
    }
}
