package com.johnnymolina.popularmovies.mvp.mainSearch;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.popularmovies.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.popularmovies.api.model.modelRetrofit.MovieInfo;

import java.util.List;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface SearchListView extends MvpView {

    void setData(List<MovieInfo> list);

    void showLoading();

    void showError(Throwable e);

    void showSearchList();

    void setRealmData(List<RealmReturnedMovie> realmList);
}
