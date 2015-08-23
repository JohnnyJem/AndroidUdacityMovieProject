package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmReturnedMovie;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;

import java.util.List;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface SearchListView extends MvpView {

    public void setData(List<MovieInfo> list);

    public void showLoading();

    public void showError(Throwable e);

    public void showSearchList();

    public void setRealmData(List<RealmReturnedMovie> realmList);
}
