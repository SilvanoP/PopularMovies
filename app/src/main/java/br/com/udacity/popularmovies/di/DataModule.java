package br.com.udacity.popularmovies.di;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import javax.inject.Singleton;

import br.com.udacity.popularmovies.data.database.PopularMoviesDatabase;
import br.com.udacity.popularmovies.data.repository.MoviesRepositoryImpl;
import br.com.udacity.popularmovies.data.webservice.CacheInterceptor;
import br.com.udacity.popularmovies.data.webservice.TheMovieDBService;
import br.com.udacity.popularmovies.feature.shared.MoviesRepository;
import br.com.udacity.popularmovies.util.Constants;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    @Provides
    @Singleton
    CacheInterceptor providesCacheInterceptor() {
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        cacheInterceptor.setOnline(true);

        return cacheInterceptor;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Context context, CacheInterceptor interceptor) {
        long cacheSize = 5*1024*1024; // 5Mb
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return new OkHttpClient.Builder().cache(cache).addInterceptor(interceptor).build();
    }

    @Provides
    @Singleton
    TheMovieDBService providesTheMovieDBClient(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(TheMovieDBService.class);
    }

    @Provides
    @Singleton
    PopularMoviesDatabase providesPopularMoviesDatabase(Context context) {
        return Room.databaseBuilder(context, PopularMoviesDatabase.class, Constants.Database.NAME)
                .build();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    MoviesRepository providesMoviesRepository(Context context, SharedPreferences preferences,
                                              TheMovieDBService service, PopularMoviesDatabase database,
                                              CacheInterceptor interceptor) {
        return new MoviesRepositoryImpl(context, preferences, service, database, interceptor);
    }
}
