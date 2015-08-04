package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.johnnymolina.androidudacitymovieproject.api.MovieService;
import com.johnnymolina.androidudacitymovieproject.api.NetworkModule;
import com.johnnymolina.androidudacitymovieproject.api.model.MovieSearchResponse;
import com.johnnymolina.androidudacitymovieproject.api.model.Result;
import com.johnnymolina.androidudacitymovieproject.mvp.mainSearch.SearchListView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public class DetailsFragPresenter extends MvpBasePresenter<DetailsFragView> {

    Result result;

    public DetailsFragPresenter() {
    }

    public void onFragStart(){
        if(isViewAttached()){

            if (result!=null){
                getView().showLoading();
                getView().setData(result);
                getView().showSearchList();
            }
        }
    }

    public void presentDetails(Result event) {
        //update View
        if (isViewAttached()) {
            this.result = event;
            getView().showSearchList();//If view IS attached then show the searchList
            getView().setData(this.result);

        }

    }

}
