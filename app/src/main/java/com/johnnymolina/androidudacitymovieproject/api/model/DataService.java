package com.johnnymolina.androidudacitymovieproject.api.model;


import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Returned;

import java.util.List;

import rx.Observable;

/* github author: kboyarshinov/realm-rxjava-example */
public interface DataService {
    public Observable<List<Returned>> returnedList();
    public Observable<Returned> newReturnedList(List<Info> infoList);
}