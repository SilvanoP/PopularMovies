package br.com.udacity.popularmovies.di;

import android.app.Application;

import javax.inject.Singleton;

import br.com.udacity.popularmovies.PopularMoviesApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ContextModule.class,
        DataModule.class,
        ActivityModule.class,
        AndroidSupportInjectionModule.class
})
public interface PopularMoviesComponent extends AndroidInjector<PopularMoviesApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        PopularMoviesComponent build();
    }

    void inject(PopularMoviesApplication application);
}
