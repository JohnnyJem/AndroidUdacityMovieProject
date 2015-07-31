package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.api.model.MovieSearchResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
//TODO: Fix Model and Result Service to attach successfully with the setData() and the searchListAAdapter
public class SearchListPresenter extends MvpBasePresenter<SearchListView> {
    MovieService movieService;

    public SearchListPresenter(MovieService movieService) {
        this.movieService = movieService;
    }

    public void searchForMovies(String query) {
        //update View
        if (isViewAttached()) {
            getView().showLoading();//grabbing the view reference
        }
        //we ask the presenter to perform a search with a query
        movieService.movieSearch(query, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                .subscribe(new Subscriber<MovieSearchResponse>() {//Attaching subscriber of type ____SearchResponse to the Observable
                    @Override
                    public void onCompleted() {//This is a callback that notifies the observer of the end of the sequence.
                        if (isViewAttached()) {
                            getView().showSearchList();//If view IS attached then show the searchList
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showError(e);
                        }
                    }

                    @Override
                    public void onNext(MovieSearchResponse movieSearchResponse) {
                        List<Result> list= movieSearchResponse.getResults();
                        if (isViewAttached()) {
                            getView().setData(list);
                        }
                    }
                });
    }
}
