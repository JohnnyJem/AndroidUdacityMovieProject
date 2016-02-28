package com.johnnymolina.popularmovies.mvp.detailsView;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieMedia;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieReview;

import java.util.List;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface DetailsFragView extends MvpView {

    void setData(RealmMovieInfo movieInfo);

    void setRealmData(RealmReturnedMovie realmReturnedMovie);


    void setDataMedia(List<MovieMedia> resultsMedia);

    void setDataReview(List<MovieReview> resultsReview);

    void showLoading();

    void showError(Throwable e);

    void showSearchList();


    RealmMovieInfo getRealmMovieInfo();
}
