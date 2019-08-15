package br.com.udacity.popularmovies;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import br.com.udacity.popularmovies.di.DaggerPopularMoviesComponent;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class PopularMoviesApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerPopularMoviesComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }
}
