package com.johnnymolina.androidudacitymovieproject.api.model.modelRealm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.johnnymolina.androidudacitymovieproject.api.model.modelRetrofit.Result;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Johnny on 8/12/2015.
 */
public class RealmResponseSearchMovie extends RealmObject {

    private int page;

    private RealmList<RealmResult> results = new RealmList<>();

    private int totalPages;

    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public RealmList<RealmResult> getResults() {
        return results;
    }

    public void setResults(RealmList<RealmResult> results) {
        this.results = results;
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
