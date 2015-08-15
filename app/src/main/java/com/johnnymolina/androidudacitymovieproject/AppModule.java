package com.johnnymolina.androidudacitymovieproject;

import android.app.Application;
import android.content.Context;

import com.johnnymolina.androidudacitymovieproject.api.model.DataService;
import com.johnnymolina.androidudacitymovieproject.api.model.RealmDataService;
import com.johnnymolina.androidudacitymovieproject.eventBus.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
//This is a module - it contains methods that provide dependencies

@Module
public class AppModule {
    private MovieApplication movieApplication;
    private RxBus _rxBus = null;
    private DataService dataService;

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
    public DataService provideDataService(MovieApplication movieApplication){
        if (dataService==null) {
            dataService = new RealmDataService(movieApplication);
        }
        return dataService;
    }

}
