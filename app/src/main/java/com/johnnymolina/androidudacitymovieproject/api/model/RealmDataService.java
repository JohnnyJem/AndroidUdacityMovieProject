package com.johnnymolina.androidudacitymovieproject.api.model;


import android.content.Context;

import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieReview;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Media;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Returned;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Review;
import com.johnnymolina.androidudacitymovieproject.api.model.rx.RealmObservable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/* github author: kboyarshinov/realm-rxjava-example */

public class RealmDataService implements DataService {
    private final Context context;

    public RealmDataService(Context context) {
        this.context = context; // should always be an Application context.
    }

    @Override
    public Observable<List<Returned>> returnedList(){
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmReturnedMovie>>() {
            @Override
            public RealmResults<RealmReturnedMovie> call(Realm realm) {
                // find all issues
                return realm.where(RealmReturnedMovie.class).findAll();

            }
        }).map(new Func1<RealmResults<RealmReturnedMovie>, List<Returned>>() {
            @Override
            public List<Returned> call(RealmResults<RealmReturnedMovie> realmReturnedMovies) {
                // map them to UI objects
                final List<Returned> returnedMovies = new ArrayList<>(realmReturnedMovies.size());
                for (RealmReturnedMovie realmReturnedMovie : realmReturnedMovies) {
                    returnedMovies.add(returnedMoviesFromRealm(realmReturnedMovie));
                }
                return returnedMovies;
            }
        });
    }

    @Override
    public Observable<Returned> newReturnedList(final int id, Info info, List<Media> mediaList, List<Review> reviewList) {
        // map internal UI objects to Realm objects

        final RealmMovieInfo realmMovieInfo = new RealmMovieInfo();
        realmMovieInfo.setId(info.getId());
        realmMovieInfo.setTitle(info.getTitle());
        realmMovieInfo.setPosterPath(info.getPosterPath());
        realmMovieInfo.setReleaseDate(info.getReleaseDate());
        realmMovieInfo.setVoteAverage(info.getVoteAverage());
        realmMovieInfo.setOverview(info.getOverview());

        final RealmList<RealmMovieMedia> realmMediaList = new RealmList<RealmMovieMedia>();
        for (Media media : mediaList){
        RealmMovieMedia realmMovieMedia = new RealmMovieMedia();

            realmMovieMedia.setId(String.valueOf(info.getId()));
            realmMovieMedia.setName(media.getName());
            realmMovieMedia.setSite(media.getSite());

            realmMediaList.add(realmMovieMedia);
        }

        final RealmList<RealmMovieReview> realmMovieReviewList = new RealmList<RealmMovieReview>();
        for (Review review : reviewList){
            RealmMovieReview realmMovieReview = new RealmMovieReview();

                realmMovieReview.setId(String.valueOf(info.getId()));
                realmMovieReview.setAuthor(review.getAuthor());
                realmMovieReview.setContent(review.getContent());
                realmMovieReview.setUrl(review.getUrl());

                realmMovieReviewList.add(realmMovieReview);
        }

        return RealmObservable.object(context, new Func1<Realm, RealmReturnedMovie>() {
            @Override
            public RealmReturnedMovie call(Realm realm) {
                // internal object instances are not created by realm
                // saving them using copyToRealm returning instance associated with realm
                RealmMovieInfo movieInfo = realm.copyToRealm(realmMovieInfo);

                RealmList<RealmMovieMedia> medias = new RealmList<RealmMovieMedia>();
                for (RealmMovieMedia realmMedia : realmMediaList){
                    medias.add(realm.copyToRealm(realmMedia));
                }

                RealmList<RealmMovieReview> reviews = new RealmList<RealmMovieReview>();
                for (RealmMovieReview realmReview : realmMovieReviewList){
                    reviews.add(realm.copyToRealm(realmReview));
                }
                // create RealmIssue instance and save it
                RealmReturnedMovie returnedMovies = new RealmReturnedMovie();
                returnedMovies.setId(id);
                returnedMovies.setRealmMovieInfo(movieInfo);
                returnedMovies.setRealmMediaList(medias);
                returnedMovies.setRealmReviewList(reviews);

                return realm.copyToRealm(returnedMovies);
            }
        }).map(new Func1<RealmReturnedMovie, Returned>() {
            @Override
            public Returned call(RealmReturnedMovie realmReturnedMovies) {
                // map to UI object
                return returnedMoviesFromRealm(realmReturnedMovies);
            }
        });
    }

    private static Returned returnedMoviesFromRealm(RealmReturnedMovie realmReturnedMovie) {

        final int id = realmReturnedMovie.getId();

        final Info info = infoFromRealm(realmReturnedMovie.getRealmMovieInfo());

        final RealmList<RealmMovieMedia> realmMovieMedias = realmReturnedMovie.getRealmMediaList();
        final List<Media> medias = new ArrayList<>(realmMovieMedias.size());

        for (RealmMovieMedia realmMedia : realmMovieMedias) {
            medias.add(mediaFromRealm(realmMedia));
        }

        final RealmList<RealmMovieReview> realmMovieReviews = realmReturnedMovie.getRealmReviewList();
        final List<Review> reviews = new ArrayList<>(realmMovieReviews.size());
        for (RealmMovieReview realmReview : realmMovieReviews ){
            reviews.add(reviewFromRealm(realmReview));
        }

        return new Returned(id,info,medias,reviews);
    }

   // private static User userFromRealm(RealmUser realmUser) {  // An extra method example for future use
     //   return new User(realmUser.getLogin());
    //}
    private static Info infoFromRealm(RealmMovieInfo realmMovieInfo) {
        return new Info(realmMovieInfo.getId(),realmMovieInfo.getTitle(),realmMovieInfo.getPosterPath(),realmMovieInfo.getReleaseDate(),realmMovieInfo.getVoteAverage(),realmMovieInfo.getOverview());
    }
    private static Media mediaFromRealm(RealmMovieMedia realmMovieMedia){
        return new Media(realmMovieMedia.getId(),realmMovieMedia.getName(),realmMovieMedia.getSite());
    }
    private static Review reviewFromRealm(RealmMovieReview realmMovieReview){
        return new Review(realmMovieReview.getId(),realmMovieReview.getAuthor(),realmMovieReview.getContent(),realmMovieReview.getUrl());
    }

}
