package com.johnnymolina.androidudacitymovieproject.api.model;


import android.content.Context;

import com.johnnymolina.androidudacitymovieproject.api.model.modelPogo.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelPogo.Returned;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovies;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.rx.RealmObservable;

import java.util.ArrayList;
import java.util.List;

import clojure.asm.Label;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/* github author: kboyarshinov/realm-rxjava-example */

public class RealmDataService implements DataService {
    private final Context context;

    public RealmDataService(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Observable<List<Returned>> returnedList(){
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmReturnedMovies>>() {
            @Override
            public RealmResults<RealmReturnedMovies> call(Realm realm) {
                // find all issues
                return realm.where(RealmReturnedMovies.class).findAll();

            }
        }).map(new Func1<RealmResults<RealmReturnedMovies>, List<Returned>>() {
            @Override
            public List<Returned> call(RealmResults<RealmReturnedMovies> realmReturnedMovies) {
                // map them to UI objects
                final List<Returned> returnedMovies = new ArrayList<>(realmReturnedMovies.size());
                for (RealmReturnedMovies realmReturnedMovie : realmReturnedMovies) {
                    returnedMovies.add(returnedMoviesFromRealm(realmReturnedMovie));
                }
                return returnedMovies;
            }
        });
    }

    @Override
    public Observable<Returned> newReturnedList(List <Info> infoList) {
        // map internal UI objects to Realm objects
        final RealmList<RealmMovieInfo> realmMovieInfos = new RealmList<RealmMovieInfo>();
        for (Info info : infoList) {
            RealmMovieInfo realmMovieInfo = new RealmMovieInfo();
            realmMovieInfo.setId(info.getId());
            realmMovieInfo.setTitle(info.getTitle());
            realmMovieInfo.setPosterPath(info.getPosterPath());
            realmMovieInfo.setReleaseDate(info.getReleaseDate());
            realmMovieInfo.setVoteAverage(info.getVoteAverage());
            realmMovieInfo.setOverview(info.getOverview());
            realmMovieInfos.add(realmMovieInfo);
        }
        return RealmObservable.object(context, new Func1<Realm, RealmReturnedMovies>() {
            @Override
            public RealmReturnedMovies call(Realm realm) {
                // internal object instances are not created by realm
                // saving them using copyToRealm returning instance associated with realm
                RealmList<RealmMovieInfo> movieInfos = new RealmList<RealmMovieInfo>();
                for (RealmMovieInfo realmMovieInfo : realmMovieInfos) {
                    movieInfos.add(realm.copyToRealm(realmMovieInfo));
                }
                // create RealmIssue instance and save it
                RealmReturnedMovies returnedMovies = new RealmReturnedMovies();
                returnedMovies.setRealmMovieInfos(movieInfos);
                return realm.copyToRealm(returnedMovies);
            }
        }).map(new Func1<RealmReturnedMovies, Returned>() {
            @Override
            public Returned call(RealmReturnedMovies realmReturnedMovies) {
                // map to UI object
                return returnedMoviesFromRealm(realmReturnedMovies);
            }
        });
    }

    private static Returned returnedMoviesFromRealm(RealmReturnedMovies realmReturnedMovies) {
        //final User user = userFromRealm(realmIssue.getUser());  could be used for reviews and etc
        final RealmList<RealmMovieInfo> realmMovieInfos = realmReturnedMovies.getRealmMovieInfos();
        final List<Info> infos = new ArrayList<>(realmMovieInfos.size());
        for (RealmMovieInfo realmMovieInfo : realmMovieInfos) {
            infos.add(infoFromRealm(realmMovieInfo));
        }
        return new Returned(infos);
    }

   // private static User userFromRealm(RealmUser realmUser) {  // An extra method example for future use
     //   return new User(realmUser.getLogin());
    //}

    private static Info infoFromRealm(RealmMovieInfo realmMovieInfo) {
        return new Info(realmMovieInfo.getId(),realmMovieInfo.getTitle(),realmMovieInfo.getPosterPath(),realmMovieInfo.getReleaseDate(),realmMovieInfo.getVoteAverage(),realmMovieInfo.getOverview());
    }




}
