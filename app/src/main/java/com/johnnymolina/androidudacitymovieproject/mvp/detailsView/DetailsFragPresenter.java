package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import android.content.SharedPreferences;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieReview;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieReview;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedReviews;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieMedia;


import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public class DetailsFragPresenter extends MvpBasePresenter<DetailsFragView> {
    private final String TAG = getClass().getName().toString();
    private final String APPLICATION_TAG = "com.johnnymolina.androidudacitymovieproject";
    private final String MOVIE_ID = "MOVIE_ID";
    MovieService movieService;

    int movieID = 0;
    RealmMovieInfo realmMovieInfo;
    List<MovieMedia> returnedMediaList;
    List<MovieReview> returnedReviewList;

    RealmReturnedMovie realmReturnedMovie;

    String previousMediaQuery;
    String previousReviewQuery;
    boolean realmSaveState = false;
    int LOADSTATE = 0; //Default. 1 = Media loaded. 2 = Reviews & Media loaded and available for saving to Realm;
    private boolean movieSaveState;

    public DetailsFragPresenter(MovieService movieService) {
        this.movieService = movieService;
    }

    public void initFrag(Realm realm, SharedPreferences sharedPreferences, int movieID){
        if (movieID == 0){
            this.movieID = movieID;
        }
        //checking if our movieID can be found in our sharedPreferences
        if (sharedPreferences.getInt(String.valueOf(movieID),0) > 0){
            //note: sharedPreferences lookup is not necessary. A simple lookup of all saved RealmObjects to check if any match
            //the given movieID would suffice, but sharedPreferences storage & lookup is part of the rubric.
            //TODO: implement sharedPreferences and Realm Lookup
            Log.i("LOOKUP", String.valueOf(movieID) + " exists in SharedPreferences");
            RealmReturnedMovie query = realm.where(RealmReturnedMovie.class)
                    .equalTo("id",movieID)
                    .findFirst();
            RealmReturnedMovie realmReturnedMovie = new RealmReturnedMovie();
            realmReturnedMovie.setRealmMovieInfo(query.getRealmMovieInfo());
            realmReturnedMovie.setRealmMediaList(query.getRealmMediaList());
            realmReturnedMovie.setRealmReviewList(query.getRealmReviewList());
            if (isViewAttached()) {
                setRealmDetails(realmReturnedMovie); // change this to lookup from realm
            }
        }else {
            if (isViewAttached()){
                if (getView().getRealmMovieInfo() !=null) {
                    Log.i("LOOKUP",String.valueOf(movieID)+" DOES NOT exist in SharedPreferences");
                    setDetails(getView().getRealmMovieInfo());
                }
            }
        }

    }

    public void setDetails(RealmMovieInfo currentMovieInfo) {
        if (isViewAttached()) {
            if (this.realmMovieInfo == null) {
                this.realmMovieInfo = currentMovieInfo;
            }


            getView().setData(realmMovieInfo);
            getView().showSearchList();//If view IS attached then show the searchList
            movieID = realmMovieInfo.getId();

            if (returnedMediaList !=null) {
                getView().setDataMedia(returnedMediaList);
            }else{
                requestMovieMedia();
            }
            if (returnedReviewList !=null) {
                getView().setDataReview(returnedReviewList);
            }else{
                requestMovieReviews();
            }
        }
    }

    public void setRealmDetails(RealmReturnedMovie currentRealmMovie){
        if (this.realmReturnedMovie == null){
            this.realmReturnedMovie = currentRealmMovie;
        }
        getView().setRealmData(this.realmReturnedMovie);
        getView().showSearchList();
    }

        public void requestMovieMedia() {
            String query =Integer.toString(realmMovieInfo.getId());
            previousMediaQuery = query;

            //update View
            if (isViewAttached()) {
                getView().showLoading();//grabbing the view reference
            }
            //we ask the presenter to perform a search with a query
            movieService.movieMediaRequest(query, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                    .delay(3, TimeUnit.SECONDS) //wait 5 seconds
                    .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                    .subscribe(new Subscriber<ReturnedMedia>() {//Attaching subscriber of type _________Response to the Observable
                        @Override
                        public void onCompleted() {//This is a callback that notifies the observer of the end of the sequence.
                            if (isViewAttached()) {
                                getView().showSearchList();//If view IS attached then show the searchList
                                LOADSTATE++;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached()) {
                                getView().showError(e);
                            }
                        }

                        @Override
                        public void onNext(ReturnedMedia movieMediaRequestResponse) {
                            returnedMediaList = movieMediaRequestResponse.getResultsMedia();
                            //Todo: convert this Retrofit object into an immutable pojo too
                            if (isViewAttached()) {
                                getView().setDataMedia(returnedMediaList);
                            }
                        }
                    });
        }

    public void requestMovieReviews() {
        String queryReview =Integer.toString(realmMovieInfo.getId());
        previousReviewQuery = queryReview;
        //update View
        if (isViewAttached()) {
            getView().showLoading();//grabbing the view reference
        }
        //we ask the presenter to perform a search with a query
        movieService.movieReviewRequest(queryReview, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                .delay(3, TimeUnit.SECONDS) //wait 5 seconds
                .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                .subscribe(new Subscriber<ReturnedReviews>() {//Attaching subscriber of type _________Response to the Observable
                    @Override
                    public void onCompleted() {//This is a callback that notifies the observer of the end of the sequence.
                        if (isViewAttached()) {
                            getView().showSearchList();//If view IS attached then show the searchList
                            LOADSTATE++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showError(e);
                        }
                    }

                    @Override
                    public void onNext(ReturnedReviews movieReviewRequestResponse) {
                        returnedReviewList = movieReviewRequestResponse.getResultsReview();
                        //Todo: convert this Retrofit object into an immutable pojo too.
                        if (isViewAttached()) {
                            getView().setDataReview(returnedReviewList);
                        }
                    }
                });
    }

    public void addMovieToRealm(Realm realm, SharedPreferences sharedPreferences) {
        if (LOADSTATE > 1 && movieID > 0) {
                sharedPreferences.edit().putInt(String.valueOf(movieID), movieID).commit();
                realm.beginTransaction();
                RealmReturnedMovie returnedMovie = realm.createObject(RealmReturnedMovie.class);
                returnedMovie.setId(movieID);
                RealmMovieInfo movieInfo = realm.copyToRealm(realmMovieInfo);
                returnedMovie.setRealmMovieInfo(movieInfo);
                //returnedMovie.getRealmMovieInfo().setId(movieID);
                //returnedMovie.getRealmMovieInfo().setTitle(realmMovieInfo.getTitle());
                //returnedMovie.getRealmMovieInfo().setPosterPath(realmMovieInfo.getPosterPath());
                //returnedMovie.getRealmMovieInfo().setReleaseDate(realmMovieInfo.getReleaseDate());
                //returnedMovie.getRealmMovieInfo().setVoteAverage(realmMovieInfo.getVoteAverage());
                //returnedMovie.getRealmMovieInfo().setOverview(realmMovieInfo.getOverview());
                if (returnedMediaList!=null){
                    for (MovieMedia returnedMedia : returnedMediaList){
                        RealmMovieMedia movieMedia = new RealmMovieMedia();
                        movieMedia.setId(returnedMedia.getId());
                        movieMedia.setName(returnedMedia.getName());
                        movieMedia.setKey(returnedMedia.getKey());
                        RealmMovieMedia realmMovieMedia = realm.copyToRealm(movieMedia);
                        returnedMovie.getRealmMediaList().add(realmMovieMedia);
                    }
                }
                if (returnedReviewList!=null){
                    for (MovieReview returnedReview : returnedReviewList){
                        RealmMovieReview movieReview = new RealmMovieReview();
                        movieReview.setId(returnedReview.getId());
                        movieReview.setAuthor(returnedReview.getAuthor());
                        movieReview.setUrl(returnedReview.getUrl());
                        movieReview.setContent(returnedReview.getContent());
                        RealmMovieReview realmMovieReview = realm.copyToRealm(movieReview);
                        returnedMovie.getRealmReviewList().add(realmMovieReview);
                    }
                }
                realm.commitTransaction();
        }else{
            Log.d("AddMovie", "Failed: All Movie components not yet loaded. Try again "); //TODO: make this notification into a snackbar
        }
    }

}
