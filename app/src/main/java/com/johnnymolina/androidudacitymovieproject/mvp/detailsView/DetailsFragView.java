package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieInfo;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieMedia;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.MovieReviews;

import java.util.List;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface DetailsFragView extends MvpView {

    public void setData(MovieInfo movieInfo);

    public void setDataMedia(List<MovieMedia> resultsMedia);

    public void setDataReview(List<MovieReviews> resultsReview);

    public void showLoading();

    public void showError(Throwable e);

    public void showSearchList();
}
