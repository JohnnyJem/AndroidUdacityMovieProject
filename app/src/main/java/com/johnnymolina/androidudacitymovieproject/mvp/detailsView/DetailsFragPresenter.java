package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieReviews;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedReviews;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieMedia;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public class DetailsFragPresenter extends MvpBasePresenter<DetailsFragView> {
    MovieService movieService;
    List<MovieMedia> resultsMediaList;
    List<MovieReviews> resultsReviewList;
    MovieInfo movieInfo;
    String previousMediaQuery;
    String previousReviewQuery;


    public DetailsFragPresenter(MovieService movieService) {
        this.movieService = movieService;
    }

    public void setDetails(MovieInfo currentMovieInfo) {
        if (isViewAttached()) {
            if (this.movieInfo ==null) {
                this.movieInfo = currentMovieInfo;
                }

            getView().showSearchList();//If view IS attached then show the searchList
            getView().setData(movieInfo);
            if (resultsMediaList!=null) {
                getView().setDataMedia(resultsMediaList);
            }else{
                requestMovieMedia();
            }
            if (resultsReviewList!=null) {
                getView().setDataReview(resultsReviewList);
            }else{
                requestMovieReviews();
            }
        }
    }


        public void requestMovieMedia() {
            String query =Integer.toString(movieInfo.getId());
            previousMediaQuery = query;

            //update View
            if (isViewAttached()) {
                getView().showLoading();//grabbing the view reference
            }
            //we ask the presenter to perform a search with a query
            movieService.movieMediaRequest(query, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                    .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                    .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                    .subscribe(new Subscriber<ReturnedMedia>() {//Attaching subscriber of type _________Response to the Observable
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
                        public void onNext(ReturnedMedia movieMediaRequestResponse) {
                            resultsMediaList = movieMediaRequestResponse.getResultsMedia();
                            if (isViewAttached()) {
                                getView().setDataMedia(resultsMediaList);
                            }
                        }
                    });
        }

    public void requestMovieReviews() {
        String queryReview =Integer.toString(movieInfo.getId());
        previousReviewQuery = queryReview;
        //update View
        if (isViewAttached()) {
            getView().showLoading();//grabbing the view reference
        }
        //we ask the presenter to perform a search with a query
        movieService.movieReviewRequest(queryReview, NetworkModule.API_KEY) //subscribes to the Observable provided by Retrofit and lets the View know what to display
                .delay(5, TimeUnit.SECONDS) //wait 5 seconds
                .observeOn(AndroidSchedulers.mainThread())  //Declaring that our observable be observed on the main thread
                .subscribe(new Subscriber<ReturnedReviews>() {//Attaching subscriber of type _________Response to the Observable
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
                    public void onNext(ReturnedReviews movieReviewRequestResponse) {
                        resultsReviewList = movieReviewRequestResponse.getResultsReview();
                        if (isViewAttached()) {
                            getView().setDataReview(resultsReviewList);
                        }
                    }
                });
    }


}
