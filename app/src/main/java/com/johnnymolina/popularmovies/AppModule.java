package com.johnnymolina.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;

import com.johnnymolina.popularmovies.eventBus.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
//This is a module - it contains methods that provide dependencies

@Module
public class AppModule {
    private final String APPLICATION_TAG = "com.johnnymolina.androidudacitymovieproject";
    private MovieApplication movieApplication;
    private RxBus _rxBus = null;
    private SharedPreferences sharedPreferences;
    private Realm realm;

    public AppModule(MovieApplication movieApplication){
        this.movieApplication = movieApplication;
    }

    //Providing ApplicationContext to enable references that survive
    //during the lifetime of the application
    @Provides @Singleton
    public MovieApplication provideMovieApplication(){
        return this.movieApplication;
    }

    @Provides @Singleton
    public RxBus provideRxBus() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }
        return _rxBus;
    }


    @Provides @Singleton
    public SharedPreferences provideSharedPreferences (MovieApplication movieApplication){
        if (sharedPreferences == null){
           sharedPreferences = movieApplication.getSharedPreferences(APPLICATION_TAG, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    @Provides @Singleton
    public Realm provideRealm (MovieApplication movieApplication){
        if (realm == null){
            realm = Realm.getInstance(movieApplication);
        }
        return realm;
    }

}
