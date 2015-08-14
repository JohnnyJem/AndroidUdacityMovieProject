package com.johnnymolina.androidudacitymovieproject.api.model.modelRealm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Johnny on 8/13/2015.
 */
public class RealmResponseMediaRequest extends RealmObject {

    private int id;

    private RealmList<RealmResultMedia> resultsMedia = new RealmList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<RealmResultMedia> getResultsMedia() {
        return resultsMedia;
    }

    public void setResultsMedia(RealmList<RealmResultMedia> resultsMedia) {
        this.resultsMedia = resultsMedia;
    }
}
