package com.johnnymolina.androidudacitymovieproject;

import com.google.gson.Gson;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.mvp.detailsView.DetailsFrag;
import com.johnnymolina.androidudacitymovieproject.mvp.mainSearch.ActivityMain;
import com.johnnymolina.androidudacitymovieproject.mvp.mainSearch.SearchFragment;
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


    public MovieApplication movieApplication();

    public OkHttpClient okHttpClient();

    public Gson gson();

    public RestAdapter restAdapter();

    public MovieService movieService();


    //public ReposAdapter adapter();
}
