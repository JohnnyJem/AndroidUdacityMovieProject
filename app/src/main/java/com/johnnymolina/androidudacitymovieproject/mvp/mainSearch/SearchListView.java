package com.johnnymolina.androidudacitymovieproject.mvp.mainSearch;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.androidudacitymovieproject.api.model.RealmDataService;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface SearchListView extends MvpView {

    public void setData(List<MovieInfo> list);

    public void showLoading();

    public void showError(Throwable e);

    public void showSearchList();
}
