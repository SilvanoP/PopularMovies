package br.com.udacity.popularmovies.di;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    @Provides
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }
}
