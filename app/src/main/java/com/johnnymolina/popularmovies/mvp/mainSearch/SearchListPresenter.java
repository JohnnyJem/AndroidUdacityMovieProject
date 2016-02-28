package com.johnnymolina.popularmovies.mvp.mainSearch;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.popularmovies.api.MovieService;
import com.johnnymolina.popularmovies.api.NetworkModule;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.ReturnedMovies;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
//TODO: Fix Model and Info Service to attach successfully with the setData() and the searchListAAdapter
public class SearchListPresenter extends MvpBasePresenter<SearchListView> {
    String TAG = getClass().getName().toString();

    MovieService movieService;
    CompositeSubscription compositeSubscription;
    List<MovieInfo> list;
    List<RealmReturnedMovie> realmList;
    String previousQuery;
    boolean isRealmAdapterActive = false;

    public SearchListPresenter(MovieService movieService) {
        this.movieService = movieService;
    }

    public void setMovies(Realm realm){
        if (list!=null && !isRealmAdapterActive) {
            getView().setData(list);
            getView().showSearchList();
        }else if (!isRealmAdapterActive){
            searchForMovies(previousQuery);
        }

        if (realmList !=null && isRealmAdapterActive){
            getView().setRealmData(realmList);
            getView().showSearchList();
        }else if (isRealmAdapterActive){
            //search with realm for all saved movies
            searchForRealmMovies(realm);
        }


    }

    public void searchForRealmMovies(Realm realm) {
        ArrayList<RealmReturnedMovie> realmReturnedMovies = new ArrayList<>();
        RealmResults<RealmReturnedMovie> query = realm.where(RealmReturnedMovie.class).findAll();
        for (RealmReturnedMovie p : query){
            realmReturnedMovies.add(p);
        }
        realmList = realmReturnedMovies;
        getView().setRealmData(realmList);
        getView().showSearchList();
        isRealmAdapterActive = true;
    }

    public void searchForMovies(String query) {
        previousQuery = query;
        //update View
        if (isViewAttached()) {
            getView().showLoading();//grabbing the view reference
        }
        //we ask the presenter to perform a search with a query
        movieService.movieSearch(previousQuery, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                .subscribe(new Subscriber<ReturnedMovies>() {//Attaching subscriber of type ____SearchResponse to the Observable
                    @Override
                    public void onCompleted() {//This is a callback that notifies the observer of the end of the sequence.
                        if (isViewAttached()) {
                            getView().showSearchList();//If view IS attached then show the searchList
                            isRealmAdapterActive = false;
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
                        list = movieSearchResponse.getMovieInfos();
                        //Todo: convert this Retrofit object into an immutable pojo too
                        // map internal UI objects to Realm objects
                        if (isViewAttached()) {
                            getView().setData(list);
                        }
                    }
                });
    }

}
