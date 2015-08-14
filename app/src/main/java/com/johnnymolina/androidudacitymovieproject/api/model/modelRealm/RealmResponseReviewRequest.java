package com.johnnymolina.androidudacitymovieproject.api.model.modelRealm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Johnny on 8/13/2015.
 */
public class RealmResponseReviewRequest extends RealmObject{

    private int id;

    private RealmList<RealmResultReview> resultsReview = new RealmList<>();

}
