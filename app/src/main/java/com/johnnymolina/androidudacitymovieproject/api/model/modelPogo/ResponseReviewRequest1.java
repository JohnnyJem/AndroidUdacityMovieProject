package com.johnnymolina.androidudacitymovieproject.api.model.modelPogo;

import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.ResultReview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 8/13/2015.
 */
public class ResponseReviewRequest1 {

    private int id;

    private List<ResultReview1> resultsReview = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ResultReview1> getResultsReview() {
        return resultsReview;
    }

    public void setResultsReview(List<ResultReview1> resultsReview) {
        this.resultsReview = resultsReview;
    }



}
