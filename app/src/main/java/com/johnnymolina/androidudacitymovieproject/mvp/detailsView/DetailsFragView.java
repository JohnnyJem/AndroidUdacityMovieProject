package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmMovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieReview;

import java.util.List;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface DetailsFragView extends MvpView {

    public void setData(RealmMovieInfo movieInfo);

    public void setRealmData (RealmReturnedMovie realmReturnedMovie);


    public void setDataMedia(List<MovieMedia> resultsMedia);

    public void setDataReview(List<MovieReview> resultsReview);

    public void showLoading();

    public void showError(Throwable e);

    public void showSearchList();


    public RealmMovieInfo getRealmMovieInfo();
}
