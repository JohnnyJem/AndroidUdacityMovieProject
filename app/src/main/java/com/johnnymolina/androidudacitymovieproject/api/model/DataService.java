package com.johnnymolina.androidudacitymovieproject.api.model;


import com.johnnymolina.androidudacitymovieproject.api.model.modelPogo.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelPogo.Returned;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;

import java.util.List;

import rx.Observable;

/* github author: kboyarshinov/realm-rxjava-example */
public interface DataService {
    public Observable<List<Returned>> returnedList();
    public Observable<Returned> newReturnedList(List<Info> infoList);
}