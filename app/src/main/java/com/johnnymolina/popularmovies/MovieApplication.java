package com.johnnymolina.popularmovies;

import android.app.Application;

import com.johnnymolina.popularmovies.api.NetworkModule;

/**
 * Created by Johnny Molina on 7/19/2015.
 */

public class MovieApplication extends Application{

    private AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
