package com.johnnymolina.androidudacitymovieproject.api.model;


import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Info;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Media;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Returned;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRx.Review;

import java.util.List;

import rx.Observable;

/* github author: kboyarshinov/realm-rxjava-example */
public interface DataService {
    public Observable<List<Returned>> returnedList();
    public Observable<Returned> newReturnedList(int id,Info info,List<Media> mediaList, List<Review> reviewList);
}