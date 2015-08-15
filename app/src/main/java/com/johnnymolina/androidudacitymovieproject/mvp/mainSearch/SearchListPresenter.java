package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.DataService;
import com.johnnymolina.androidudacitymovieproject.api.model.RealmDataService;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedMovies;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Returned;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
//TODO: Fix Model and Info Service to attach successfully with the setData() and the searchListAAdapter
public class SearchListPresenter extends MvpBasePresenter<SearchListView> {
    private final String TAG = getClass().getName().toString();

    MovieService movieService;
    DataService dataService;
    CompositeSubscription compositeSubscription;
    List<MovieInfo> list;
    String previousQuery;



    public SearchListPresenter(MovieService movieService) {
        this.movieService = movieService;
    }


    public void startCompositeSubscription(Context appContext) {
        dataService = dataService == null ? new RealmDataService(appContext) : dataService;
        compositeSubscription = new CompositeSubscription();
    }

    public void stopCompositeSubscription() {
        compositeSubscription.unsubscribe();
    }


    private void requestAllMovies() {
        Subscription subscription = dataService.returnedList().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        new Action1<List<Returned>>() {
                            @Override
                            public void call(List<Returned> returnedList) {
                                Log.d(TAG, "returnedList received with size " + returnedList.size());
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Request all returnedList error", throwable);
                            }
                        }
                );

        if (compositeSubscription != null) {
            compositeSubscription.add(subscription);
        }
    }

    private void removeMovie(){

    }


    public void setMovies(){
        if (list!=null) {
            getView().setData(list);
        }else{
            searchForMovies(previousQuery);
        }
    }

    public void searchForMovies(String query) {
        previousQuery = query;
        //update View
        if (isViewAttached()) {
            getView().showLoading();//grabbing the view reference
        }
        //we ask the presenter to perform a search with a query
        movieService.movieSearch(query, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                .subscribe(new Subscriber<ReturnedMovies>() {//Attaching subscriber of type ____SearchResponse to the Observable
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
                    public void onNext(ReturnedMovies movieSearchResponse) {
                        list= movieSearchResponse.getMovieInfos();
                        // map internal UI objects to Realm objects
                        if (isViewAttached()) {
                            getView().setData(list);
                        }
                    }
                });
    }


    //Todo: Implement Realm.io here. Return data as an arraylist.
    public void searchForFavoriteMovies() {

    }
}
