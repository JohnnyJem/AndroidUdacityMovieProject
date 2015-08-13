package com.johnnymolina.androidudacitymovieproject.mvp.detailsView;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.johnnymolina.androidudacitymovieproject.api.modelUI.Result;
import com.johnnymolina.androidudacitymovieproject.api.modelUI.ResultMedia;
import com.johnnymolina.androidudacitymovieproject.api.modelUI.ResultReview;

import java.util.List;

/**
 * Created by Johnny Molina on 7/19/2015.
 */
public interface DetailsFragView extends MvpView {

    public void setData(Result result);

    public void setDataMedia(List<ResultMedia> resultsMedia);

    public void setDataReview(List<ResultReview> resultsReview);

    public void showLoading();

    public void showError(Throwable e);

    public void showSearchList();
}
