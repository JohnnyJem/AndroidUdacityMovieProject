package com.johnnymolina.androidudacitymovieproject.api.model.modelPogo;

import com.johnnymolina.androidudacitymovieproject.api.model.modelRealm.RealmResult;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.Result;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Johnny on 8/12/2015.
 */
public class ResponseSearchMovie1 {

    private int page;

    private List<Result1> results = new ArrayList<Result1>();

    private int totalPages;

    private int totalResults;

    public List<Result1> getResults() {
        return results;
    }

    public void setResults(List<Result1> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }



}
