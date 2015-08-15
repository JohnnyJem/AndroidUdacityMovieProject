package com.johnnymolina.androidudacitymovieproject.api.model.modelRealm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Johnny on 8/14/2015.
 */
public class RealmReturnedMovies extends RealmObject {

    private RealmList<RealmMovieInfo> realmMovieInfos;

    public RealmList<RealmMovieInfo> getRealmMovieInfos() {
        return realmMovieInfos;
    }

    public void setRealmMovieInfos(RealmList<RealmMovieInfo> realmMovieInfos) {
        this.realmMovieInfos = realmMovieInfos;
    }

}
