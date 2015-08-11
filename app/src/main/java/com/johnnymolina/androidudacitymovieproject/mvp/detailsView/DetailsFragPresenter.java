package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.MovieMediaRequestResponse;
import com.johnnymolina.androidudacitymovieproject.api.model.MovieReviewRequestResponse;
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.api.model.ResultMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.ResultReview;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public class DetailsFragPresenter extends MvpBasePresenter<DetailsFragView> {
    MovieService movieService;
    List<ResultMedia> resultsMediaList;
    List<ResultReview> resultsReviewList;
    Result result;
    String previousMediaQuery;
    String previousReviewQuery;


    public DetailsFragPresenter(MovieService movieService) {
        this.movieService = movieService;
    }

    public void presentDetails(Result event) {
        //update View
        if (isViewAttached()) {
            this.result = event;
            getView().showSearchList();//If view IS attached then show the searchList
            getView().setData(this.result);
        }
    }

    public void initalize(){
        if(isViewAttached()){
            if (result!=null){
                getView().setData(result);
                requestMovieMedia();
                requestMovieReviews();
            }
        }
    }


        public void requestMovieMedia() {
            String query =Integer.toString(result.getId());
            previousMediaQuery = query;

            //update View
            if (isViewAttached()) {
                getView().showLoading();//grabbing the view reference
            }
            //we ask the presenter to perform a search with a query
            movieService.movieMediaRequest(query, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                    .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                    .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                    .subscribe(new Subscriber<MovieMediaRequestResponse>() {//Attaching subscriber of type _________Response to the Observable
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
                        public void onNext(MovieMediaRequestResponse movieMediaRequestResponse) {
                            resultsMediaList = movieMediaRequestResponse.getResultsMedia();
                            if (isViewAttached()) {
                                getView().setDataMedia(resultsMediaList);
                            }
                        }
                    });
        }

    public void requestMovieReviews() {
        String queryReview =Integer.toString(result.getId());
        previousReviewQuery = queryReview;
        //update View
        if (isViewAttached()) {
            getView().showLoading();//grabbing the view reference
        }
        //we ask the presenter to perform a search with a query
        movieService.movieReviewRequest(queryReview, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                .subscribe(new Subscriber<MovieReviewRequestResponse>() {//Attaching subscriber of type _________Response to the Observable
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
                    public void onNext(MovieReviewRequestResponse movieReviewRequestResponse) {
                        resultsReviewList = movieReviewRequestResponse.getResultsReview();
                        if (isViewAttached()) {
                            getView().setDataReview(resultsReviewList);
                        }
                    }
                });
    }


}
