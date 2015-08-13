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
}
