package com.johnnymolina.popularmovies;

import com.google.gson.Gson;
import com.johnnymolina.popularmovies.api.MovieService;
import com.johnnymolina.popularmovies.api.NetworkModule;
import com.johnnymolina.popularmovies.eventBus.RxBus;
import com.johnnymolina.popularmovies.mvp.detailsView.DetailsFrag;
import com.johnnymolina.popularmovies.mvp.mainSearch.ActivityMain;
import com.johnnymolina.popularmovies.mvp.mainSearch.SearchFragment;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.RestAdapter;

/**
 * Created by Johnny Molina on 7/19/2015.
 * This is a component that bridges modules and injections.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                NetworkModule.class
        }

)

public interface AppComponent {

    void inject(ActivityMain activity);
    void inject(SearchFragment fragment);
    void inject(DetailsFrag fragment);
    void inject(MovieApplication movieApplication);




    MovieApplication movieApplication();

    RxBus rxBus();

    OkHttpClient okHttpClient();

    Gson gson();

    RestAdapter restAdapter();

    MovieService movieService();


    //public ReposAdapter adapter();
}
