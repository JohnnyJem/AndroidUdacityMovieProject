package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.DataService;
import com.johnnymolina.androidudacitymovieproject.api.model.RealmDataService;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieReview;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ReturnedReviews;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Media;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Returned;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Review;

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
public class DetailsFragPresenter extends MvpBasePresenter<DetailsFragView> {
    private final String TAG = getClass().getName().toString();

    MovieService movieService;
    DataService dataService;
    CompositeSubscription compositeSubscription;

    MovieInfo movieInfo;
    List<MovieMedia> returnedMediaList;
    List<MovieReview> returnedReviewList;

    String previousMediaQuery;
    String previousReviewQuery;

    public DetailsFragPresenter(MovieService movieService) {
        this.movieService = movieService;
    }

    public void startCompositeSubscription(Context appContext) {
        dataService = dataService == null ? new RealmDataService(appContext) : dataService;
        compositeSubscription = new CompositeSubscription();
    }

    public void stopCompositeSubscription() {
        compositeSubscription.unsubscribe();
    }

    public void requestAllMovies() {
        //grabbing a returnedList
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

    public void addNewMovie() {
        //setting our Retrofit Object Models fields to an immutable POGO to be used in the RxJava stream
        // and ultimately have its feilds set to a realm object.
        Info info = new Info(movieInfo.getId(), movieInfo.getTitle(),  movieInfo.getPosterPath(), movieInfo.getReleaseDate(),movieInfo.getVoteAverage(), movieInfo.getOverview());
        List<Media> medias= new ArrayList<>();
        List<Review> reviews = new ArrayList<>();

        for (MovieMedia media : returnedMediaList){
           medias.add(new Media(media.getId(),media.getName(),media.getSite()));
            Log.e("Media", media.getName());
        }

        for (MovieReview review : returnedReviewList){
            reviews.add(new Review(review.getId(),review.getAuthor(),review.getContent(),review.getUrl()));
            Log.e("Review", review.getAuthor());
        }

        Subscription subscription = dataService.newReturnedList(movieInfo.getId(),info,medias,reviews).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        new Action1<Returned>() {
                            @Override
                            public void call(Returned returnedList) {
                                Log.d(TAG, "Issue with title " + returnedList.getInfo().getTitle() + " successfully saved");
                                String mediaAuthor = returnedList.getMediaList().size() > 0 ? returnedList.getMediaList().get(0).getName() : "empty list";
                                Log.e("Media", mediaAuthor);
                                //Todo: change all set__() methods to accept these immutable objects instead.
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Add new issue error", throwable);
                            }
                        }
                );

        if (compositeSubscription != null) {
            compositeSubscription.add(subscription);
        }
    }

    public void setDetails(MovieInfo currentMovieInfo) {
        if (isViewAttached()) {
            if (this.movieInfo == null) {
                this.movieInfo = currentMovieInfo;
                //Todo: change this to reference and take as arguments the immutable pojo
            }

            getView().showSearchList();//If view IS attached then show the searchList
            getView().setData(movieInfo);

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
                            returnedMediaList = movieMediaRequestResponse.getResultsMedia();
                            //Todo: convert this Retrofit object into an immutable pojo too
                            if (isViewAttached()) {
                                getView().setDataMedia(returnedMediaList);
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
                        returnedReviewList = movieReviewRequestResponse.getResultsReview();
                        //Todo: convert this Retrofit object into an immutable pojo too.
                        if (isViewAttached()) {
                            getView().setDataReview(returnedReviewList);
                        }
                    }
                });
    }


}
