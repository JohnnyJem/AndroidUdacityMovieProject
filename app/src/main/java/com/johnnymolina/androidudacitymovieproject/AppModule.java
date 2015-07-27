package com.johnnymolina.androidudacitymovieproject;

import android.app.Application;
import android.content.Context;

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

    public AppModule(MovieApplication movieApplication){
        this.movieApplication = movieApplication;
    }

    //Providing ApplicationContext to enable references that survive
    //during the lifetime of the application
    @Provides @Singleton
    public MovieApplication provideMovieApplication(){
        return this.movieApplication;
    }

}
