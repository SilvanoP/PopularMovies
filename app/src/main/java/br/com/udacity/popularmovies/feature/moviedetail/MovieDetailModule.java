package br.com.udacity.popularmovies.feature.moviedetail;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MovieDetailModule {

    @Binds
    public abstract MovieDetailContract.Presenter providesPresenter(MovieDetailPresenter presenter);
}
